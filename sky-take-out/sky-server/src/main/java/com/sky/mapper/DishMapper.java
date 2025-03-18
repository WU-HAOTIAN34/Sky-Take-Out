package com.sky.mapper;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;



@Mapper
public interface DishMapper {

    @Select("select count(id) from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);

    @AutoFill(value = OperationType.INSERT)
    void save(Dish dish);

    Page<DishVO> query(DishPageQueryDTO dishPageQueryDTO);

    @Select("select status from dish where id = #{id}")
    Integer queryStatusById(Long id);

    void delete(List<Long> ids);

    @Select("select d.*, c.name as categoryName from dish d left join category c on d.category_id = c.id where d.id = #{id}")
    DishVO queryById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void edit(Dish dish);

    @Select("select d.*, c.name as categoryName from dish d left join category c on d.category_id = c.id where d.category_id = #{id} and d.status = 1")
    List<DishVO> queryByCategoryId(Long id);

    @Select("select count(1) from dish where status = #{status}")
    Integer queryCountByStatus(Integer status);


}
