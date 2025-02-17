package com.sky.service;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;



public interface CategoryService {

    /**
     *
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult query(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     *
     * @param categoryDTO
     */
    void modify(CategoryDTO categoryDTO);
}
