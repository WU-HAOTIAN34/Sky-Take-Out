package com.sky.service.impl;
import com.sky.entity.OrderDetail;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;



@Service
public class ReportServiceImp implements ReportService {


    @Autowired
    private ReportMapper reportMapper;

    public TurnoverReportVO turnover(LocalDate begin, LocalDate end){
        LocalDate now = begin;
        StringBuilder date = new StringBuilder();
        StringBuilder turn = new StringBuilder();
        while (now.isBefore(end) || now.equals(end)){
            date.append(now).append(",");
            LocalDateTime db = LocalDateTime.of(now, LocalTime.of(0,0,0));
            LocalDateTime de = LocalDateTime.of(now, LocalTime.of(23,59,59));
            BigDecimal bigDecimal = reportMapper.querySumAmountByDate(db, de);
            if (bigDecimal != null){
                turn.append(bigDecimal).append(",");
            }else{
                turn.append("0.0,");
            }
            now = now.plusDays(1);
        }
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setTurnoverList(turn.deleteCharAt(turn.length()-1).toString());
        turnoverReportVO.setDateList(date.deleteCharAt(date.length()-1).toString());
        return turnoverReportVO;
    }


    public UserReportVO userReport(LocalDate begin, LocalDate end){
        LocalDate now = begin;
        StringBuilder date = new StringBuilder();
        StringBuilder dUser = new StringBuilder();
        StringBuilder sUser = new StringBuilder();
        int num = 0;
        while (now.isBefore(end) || now.equals(end)){
            date.append(now).append(",");
            LocalDateTime db = LocalDateTime.of(now, LocalTime.of(0,0,0));
            LocalDateTime de = LocalDateTime.of(now, LocalTime.of(23,59,59));
            Integer integer = reportMapper.queryCountUser(db, de);
            if (integer!=null){
                dUser.append(integer).append(",");
                num += integer;
            }else{
                dUser.append("0,");
            }
            sUser.append(num).append(",");
            now = now.plusDays(1);
        }
        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setDateList(date.deleteCharAt(date.length()-1).toString());
        userReportVO.setNewUserList(dUser.deleteCharAt(dUser.length()-1).toString());
        userReportVO.setTotalUserList(sUser.deleteCharAt(sUser.length()-1).toString());
        return userReportVO;
    }


    public OrderReportVO orderReport(LocalDate begin, LocalDate end){
        LocalDate now = begin;
        StringBuilder date = new StringBuilder();
        StringBuilder dOrder = new StringBuilder();
        StringBuilder eOrder = new StringBuilder();
        int num1 = 0;
        int num2 = 0;
        while (now.isBefore(end) || now.equals(end)){
            date.append(now).append(",");
            LocalDateTime db = LocalDateTime.of(now, LocalTime.of(0,0,0));
            LocalDateTime de = LocalDateTime.of(now, LocalTime.of(23,59,59));
            Integer sumO = reportMapper.queryCountOrder(db, de);
            if (sumO!=null){
                dOrder.append(sumO).append(",");
                num1 += sumO;
            }else{
                dOrder.append("0,");
            }
            sumO = reportMapper.queryCountCompleteOrder(db, de);
            if (sumO!=null){
                eOrder.append(sumO).append(",");
                num2 += sumO;
            }else{
                eOrder.append("0,");
            }
            now = now.plusDays(1);
        }
        OrderReportVO orderReportVO = new OrderReportVO();
        orderReportVO.setDateList(date.deleteCharAt(date.length()-1).toString());
        orderReportVO.setOrderCountList(dOrder.deleteCharAt(dOrder.length()-1).toString());
        orderReportVO.setValidOrderCountList(eOrder.deleteCharAt(eOrder.length()-1).toString());
        orderReportVO.setValidOrderCount(num2);
        orderReportVO.setTotalOrderCount(num1);
        orderReportVO.setOrderCompletionRate((double)num2/(double)num1);
        return orderReportVO;

    }


    public SalesTop10ReportVO saleReport(LocalDate begin, LocalDate end){
        StringBuilder name = new StringBuilder();
        StringBuilder number = new StringBuilder();
        LocalDateTime db = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime de = LocalDateTime.of(end, LocalTime.MAX);
        List<OrderDetail> orderDetails = reportMapper.queryCountNumber(db, de);
        for (OrderDetail o : orderDetails) {
            name.append(o.getName()).append(",");
            number.append(o.getNumber()).append(",");
        }
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList(name.deleteCharAt(name.length()-1).toString());
        salesTop10ReportVO.setNumberList(number.deleteCharAt(number.length()-1).toString());

        return salesTop10ReportVO;

    }
}
