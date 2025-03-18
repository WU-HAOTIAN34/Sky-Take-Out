package com.sky.mapper;
import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.core.OrderComparator;
import org.springframework.core.annotation.Order;

import java.util.List;



@Mapper
public interface OrderMapper {

    void save(Orders orders);

    void update(Orders orders);

    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    Page<Orders> query(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    List<Orders> queryById(Long id);

    @Select("select count(id) from orders where status = #{status}")
    Integer queryByStatus(Integer Status);

    @Select("select * from orders")
    List<Orders> queryAll();
}
