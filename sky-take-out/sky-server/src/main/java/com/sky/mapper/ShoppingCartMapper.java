package com.sky.mapper;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;



@Mapper
public interface ShoppingCartMapper {


    ShoppingCart queryByUserIdDishSetMealID(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNum(ShoppingCart shoppingCart);

    void save(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{id}")
    List<ShoppingCart> queryShoppingCartByUserId(Long id);

    @Delete("delete from shopping_cart where user_id = #{id}")
    void clean(Long id);

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
