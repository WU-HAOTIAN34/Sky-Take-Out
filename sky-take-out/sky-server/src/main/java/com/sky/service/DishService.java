package com.sky.service;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;



public interface DishService {

    PageResult query(DishPageQueryDTO dishPageQueryDTO);

    void save(DishDTO dishDTO);

    void delete(List<Long>ids);

    DishVO queryById(Long id);

    void edit(DishDTO dishDTO);

    void modifyStatus(Integer status, Long id);

    List<DishVO> queryByCategory(Long id);

    List<DishVO> queryByCategoryWithFlavor(Long categoryId);

}
