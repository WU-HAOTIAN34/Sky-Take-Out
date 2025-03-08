package com.sky.mapper;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;



@Mapper
public interface SetMealDishMapper {

    @Select("select count(id) from setmeal_dish where dish_id = #{id}")
    Integer countByDishId(Long id);

    void save(List<SetmealDish> list);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> queryBySetMealId(Long id);

    void delete(List<Long> ids);


}
