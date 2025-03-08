package com.sky.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;



@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Transactional
    public void save(SetmealDTO setmealDTO){
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setMealMapper.save(setmeal);

        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        if (list!=null && list.size()>0){
            list.forEach(l->l.setSetmealId(setmeal.getId()));
            setMealDishMapper.save(list);
        }

    }

    public PageResult query(SetmealPageQueryDTO setmealPageQueryDTO){
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page =  setMealMapper.query(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    public SetmealVO queryById(Long id){
        SetmealVO setmealvo = setMealMapper.queryById(id);
        List<SetmealDish> list = setMealDishMapper.queryBySetMealId(id);
        setmealvo.setSetmealDishes(list);
        return setmealvo;
    }

    @Transactional
    public void edit(SetmealDTO setmealDTO){
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.edit(setmeal);

        List<Long> ids = new ArrayList<>();
        ids.add(setmealDTO.getId());
        setMealDishMapper.delete(ids);

        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        if (list!=null && list.size()>0){
            list.forEach(l->l.setSetmealId(setmealDTO.getId()));
            setMealDishMapper.save(list);
        }

    }

    @Transactional
    public void delete(List<Long> ids){

        for (Long id : ids){
            if (setMealMapper.queryStatusById(id).intValue() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        setMealDishMapper.delete(ids);
        setMealMapper.delete(ids);

    }


    public void modifyStatus(Integer status, Long id){
        if (status ==1){
            List<SetmealDish> list = setMealDishMapper.queryBySetMealId(id);
            for (SetmealDish s : list){
                if (dishMapper.queryStatusById(s.getDishId()).intValue() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setMealMapper.edit(setmeal);
    }

}
