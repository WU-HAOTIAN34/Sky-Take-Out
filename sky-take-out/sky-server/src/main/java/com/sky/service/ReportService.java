package com.sky.service;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;



public interface ReportService {

    TurnoverReportVO turnover(LocalDate begin, LocalDate end);

    UserReportVO userReport(LocalDate begin, LocalDate end);

    OrderReportVO orderReport(LocalDate begin, LocalDate end);

    SalesTop10ReportVO saleReport(LocalDate begin, LocalDate end);

    void export(HttpServletResponse httpServletResponse);
}
