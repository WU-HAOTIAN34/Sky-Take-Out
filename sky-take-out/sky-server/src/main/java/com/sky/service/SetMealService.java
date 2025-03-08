package com.sky.service;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;



public interface SetMealService {

    void save(SetmealDTO setmealDTO);

    PageResult query(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO queryById(Long id);

    void edit(SetmealDTO setmealDTO);

    void delete(List<Long> ids);

    void modifyStatus(Integer status, Long id);
}
