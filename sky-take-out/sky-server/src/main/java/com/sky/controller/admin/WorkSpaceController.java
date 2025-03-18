package com.sky.controller.admin;
import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;


    @GetMapping("/businessData")
    @ApiOperation("get today data")
    public Result<BusinessDataVO> todayData(){
        log.info("今日数据");
        BusinessDataVO today = workSpaceService.today();
        return Result.success(today);
    }


    @GetMapping("/overviewOrders")
    @ApiOperation("order overview")
    public Result<OrderOverViewVO> orderOver(){
        log.info("订单总览");
        OrderOverViewVO orderOverViewVO = workSpaceService.orderOverview();
        return Result.success(orderOverViewVO);
    }

    @GetMapping("/overviewDishes")
    @ApiOperation("dish overview")
    public Result<DishOverViewVO> dishOver(){
        log.info("菜品总览");
        DishOverViewVO orderOverViewVO = workSpaceService.dishOverview();
        return Result.success(orderOverViewVO);
    }

    @GetMapping("/overviewSetmeals")
    @ApiOperation("setmeal overview")
    public Result<SetmealOverViewVO> setOver(){
        log.info("套餐总览");
        SetmealOverViewVO orderOverViewVO = workSpaceService.setmealOverview();
        return Result.success(orderOverViewVO);
    }



}
