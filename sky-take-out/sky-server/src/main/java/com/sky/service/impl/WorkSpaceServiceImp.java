package com.sky.service.impl;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;



@Service
public class WorkSpaceServiceImp implements WorkSpaceService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    public BusinessDataVO today(){
        LocalDate now = LocalDate.now();
        LocalDateTime db = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime de = LocalDateTime.of(now, LocalTime.MAX);
        BusinessDataVO res = new BusinessDataVO();
        BigDecimal turnover = reportMapper.querySumAmountByDate(db, de);
        Integer order = reportMapper.queryCountCompleteOrder(db, de);
        Integer user = reportMapper.queryCountUser(db, de);
        Integer tOrder = reportMapper.queryCountOrder(db, de);

        double t = turnover==null ? 0.0 : turnover.doubleValue();
        int o = order==null ? 0 : order;
        int u = user==null ? 0 : user;
        double r = tOrder==null ? 1 : (double)o/(double)tOrder;
        double at = o==0 ? 0.0 : t/(double)o;

        res.setTurnover(t);
        res.setValidOrderCount(o);
        res.setNewUsers(u);
        res.setUnitPrice(at);
        res.setOrderCompletionRate(r);

        return res;

    }


    public OrderOverViewVO orderOverview(){
        OrderOverViewVO res = new OrderOverViewVO();
        int w = 0;
        int d = 0;
        int co = 0;
        int ca = 0;
        List<Orders> orders = orderMapper.queryAll();
        for (Orders o : orders){
            if (o.getStatus()==6){
                ca++;
            }else if(o.getStatus()==5){
                co++;
            } else if (o.getStatus()==3) {
                d++;
            }else if (o.getStatus()==2){
                w++;
            }
        }
        res.setAllOrders(orders.size());
        res.setCancelledOrders(ca);
        res.setCompletedOrders(co);
        res.setWaitingOrders(w);
        res.setDeliveredOrders(d);

        return res;
    }

    public DishOverViewVO dishOverview(){
        DishOverViewVO res = new DishOverViewVO();

        res.setDiscontinued(dishMapper.queryCountByStatus(0));
        res.setSold(dishMapper.queryCountByStatus(1));

        return res;
    }


    public SetmealOverViewVO setmealOverview(){
        SetmealOverViewVO res = new SetmealOverViewVO();

        res.setDiscontinued(setMealMapper.queryCountByStatus(0));
        res.setSold(setMealMapper.queryCountByStatus(1));

        return res;

    }



}
