package com.sky.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;



@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    public PageResult query(DishPageQueryDTO dishPageQueryDTO){
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.query(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Transactional
    public void save(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.save(dish);

        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()>0){
            flavors.forEach(flavor -> flavor.setDishId(id));
            dishFlavorMapper.saveBatch(flavors);
        }

    }

    @Transactional
    public void delete(List<Long> ids){

        for (Long id : ids){
            if (setMealDishMapper.countByDishId(id) != 0){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
            if (dishMapper.queryStatusById(id).intValue() == StatusConstant.ENABLE.intValue()){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        dishMapper.delete(ids);

        dishFlavorMapper.delete(ids);

    }

    public DishVO queryById(Long id){
        DishVO dishVO = dishMapper.queryById(id);
        List<DishFlavor> flavors = dishFlavorMapper.queryByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Transactional
    public void edit(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.edit(dish);
        List<Long> ids = new ArrayList<>();
        ids.add(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null){
            flavors.forEach(f->f.setDishId(dish.getId()));
            dishFlavorMapper.delete(ids);
            if (flavors.size()>0) dishFlavorMapper.saveBatch(dishDTO.getFlavors());
        }
    }

    public void modifyStatus(Integer status, Long id){
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.edit(dish);
    }

    public List<DishVO> queryByCategory(Long id){
        List<DishVO> list = dishMapper.queryByCategoryId(id);
        return list;
    }

    public List<DishVO> queryByCategoryWithFlavor(Long categoryId){
        List<DishVO> list = queryByCategory(categoryId);
        for (DishVO d : list){
            List<DishFlavor> flavors = dishFlavorMapper.queryByDishId(d.getId());
            d.setFlavors(flavors);
        }
        return list;
    }


}
