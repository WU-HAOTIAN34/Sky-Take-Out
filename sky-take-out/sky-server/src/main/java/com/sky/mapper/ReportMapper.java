package com.sky.mapper;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@Mapper
public interface ReportMapper {

    @Select("select sum(amount) from orders where status = 5 and order_time >= #{begin} and order_time <= #{end}")
    BigDecimal querySumAmountByDate(LocalDateTime begin, LocalDateTime end);

    @Select("select count(1) from user where create_time >= #{begin} and create_time <= #{end}")
    Integer queryCountUser(LocalDateTime begin, LocalDateTime end);

    @Select("select count(1) from orders where order_time >= #{begin} and order_time <= #{end}")
    Integer queryCountOrder(LocalDateTime begin, LocalDateTime end);

    @Select("select count(1) from orders where order_time >= #{begin} and order_time <= #{end} and status = 5")
    Integer queryCountCompleteOrder(LocalDateTime begin, LocalDateTime end);

    @Select("select od.name, sum(od.number) as number " +
            "from order_detail od left join orders o on od.order_id = o.id " +
            "where o.status = 5 and o.order_time >= #{begin} and o.order_time <= #{end} " +
            "group by od.name " +
            "order by number desc " +
            "limit 10")
    List<OrderDetail> queryCountNumber(LocalDateTime begin, LocalDateTime end);

}
