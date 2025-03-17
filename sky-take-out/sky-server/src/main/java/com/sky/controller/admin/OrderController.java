package com.sky.controller.admin;
import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("page")
    public Result<PageResult> getOrder(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("查询订单：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.queryOrder(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("get order number")
    public Result<OrderStatisticsVO> getOrderNumber(){
        log.info("获取统计");
        OrderStatisticsVO number = orderService.getNumber();
        return Result.success(number);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("get order detail")
    public Result<OrderVO> getOrder(@PathVariable Long id){
        log.info("获取订单信息: {}", id);
        OrderVO orderDetail = orderService.getOrderDetail(id);
        return Result.success(orderDetail);
    }

    @PutMapping("/confirm")
    @ApiOperation("confirm order")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单：{}", ordersConfirmDTO);
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("rejection")
    @ApiOperation("reject order")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单：{}", ordersRejectionDTO);
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }


    @PutMapping("/cancel")
    @ApiOperation("cancel order")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单：{}", ordersCancelDTO);
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("delivery order")
    public Result deliveryOrder(@PathVariable Long id){
        log.info("派送订单：{}", id);
        orderService.deliveryOrder(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("complete order")
    public Result finishOrder(@PathVariable Long id){
        log.info("完成订单：{}", id);
        orderService.finishOrder(id);
        return Result.success();
    }

}
