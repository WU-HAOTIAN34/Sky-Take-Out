package com.sky.controller.admin;
import com.aliyuncs.http.HttpResponse;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;



@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/turnoverStatistics")
    @ApiOperation("turnover")
    public Result<TurnoverReportVO> turnover(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计");
        TurnoverReportVO turnover = reportService.turnover(begin, end);
        return Result.success(turnover);
    }


    @GetMapping("/userStatistics")
    @ApiOperation("user")
    public Result<UserReportVO> userReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计");
        UserReportVO userReportVO = reportService.userReport(begin, end);
        return Result.success(userReportVO);
    }


    @GetMapping("/ordersStatistics")
    @ApiOperation("order report")
    public Result<OrderReportVO> orderReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计");
        OrderReportVO orderReportVO = reportService.orderReport(begin, end);
        return Result.success(orderReportVO);
    }


    @GetMapping("/top10")
    @ApiOperation("sale top 10")
    public Result<SalesTop10ReportVO> saleReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("销量统计");
        SalesTop10ReportVO salesTop10ReportVO = reportService.saleReport(begin, end);
        return Result.success(salesTop10ReportVO);
    }


    @GetMapping("/export")
    @ApiOperation("export")
    public void export(HttpServletResponse httpServletResponse){
        log.info("导出报表");
        reportService.export(httpServletResponse);

    }



}
