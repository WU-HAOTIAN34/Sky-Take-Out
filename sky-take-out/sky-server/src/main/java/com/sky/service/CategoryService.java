package com.sky.service;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;



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

    /**
     *
     * @param status
     * @param id
     */
    void editStatus(Integer status, Long id);

    /**
     *
     * @param id
     */
    void delete(Long id);

    /**
     *
     * @param type
     * @return
     */
    List<Category> queryType(Integer type);
}
