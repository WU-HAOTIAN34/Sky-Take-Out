package com.sky.service;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;



public interface SetMealService {

    void save(SetmealDTO setmealDTO);

    PageResult query(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO queryById(Long id);

    void edit(SetmealDTO setmealDTO);

    void delete(List<Long> ids);

    void modifyStatus(Integer status, Long id);

    List<SetmealVO> queryByCategoryId(Long categoryId);

    List<DishItemVO> queryDishBySetMealId(Long setMealId);
}
