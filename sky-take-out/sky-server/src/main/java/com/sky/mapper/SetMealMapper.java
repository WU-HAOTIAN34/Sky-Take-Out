package com.sky.mapper;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;



@Mapper
public interface SetMealMapper {

    @Select("select count(id) from setmeal where category_id = #{id}")
    Integer countByCategoryId(Long id);

    @AutoFill(value = OperationType.INSERT)
    void save(Setmeal setmeal);

    Page<Setmeal> query(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select s.*, c.name as categoryName from setmeal s left join category c on s.category_id = c.id where s.id = #{id}")
    SetmealVO queryById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void edit(Setmeal setmeal);

    @Select("select status from setmeal where id = #{id}")
    Integer queryStatusById(Long id);

    void delete(List<Long>ids);
}
