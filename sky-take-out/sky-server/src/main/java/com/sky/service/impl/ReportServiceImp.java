package com.sky.service.impl;
import com.sky.entity.OrderDetail;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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


    public void export(HttpServletResponse httpServletResponse){

        LocalDate now = LocalDate.now();
        LocalDateTime db = LocalDateTime.of(now.minusDays(30), LocalTime.MIN);
        LocalDateTime de = LocalDateTime.of(now.minusDays(1), LocalTime.MAX);

        BigDecimal turnover = reportMapper.querySumAmountByDate(db, de);
        Integer order = reportMapper.queryCountCompleteOrder(db, de);
        Integer user = reportMapper.queryCountUser(db, de);
        Integer tOrder = reportMapper.queryCountOrder(db, de);

        double t = turnover==null ? 0.0 : turnover.doubleValue();
        int o = order==null ? 0 : order;
        int u = user==null ? 0 : user;
        double r = tOrder==null ? 1 : (double)o/(double)tOrder;
        double at = o==0 ? 0.0 : t/(double)o;
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("excel/template.xlsx");
        try {
            XSSFWorkbook sheets = new XSSFWorkbook(resourceAsStream);
            XSSFSheet sheet = sheets.getSheet("Sheet1");
            XSSFRow row = sheet.getRow(1);

            row.getCell(1).setCellValue("时间："+db+" 到 "+de);

            row = sheet.getRow(3);

            row.getCell(2).setCellValue(t+"");
            row.getCell(4).setCellValue((r*100)+"%");
            row.getCell(6).setCellValue(u+"");

            row = sheet.getRow(4);

            row.getCell(2).setCellValue(o+"");
            row.getCell(4).setCellValue(at+"");


            for (int i=0; i<30; i++){
                db = LocalDateTime.of(now.minusDays(1+i), LocalTime.MIN);
                de = LocalDateTime.of(now.minusDays(1+i), LocalTime.MAX);

                turnover = reportMapper.querySumAmountByDate(db, de);
                order = reportMapper.queryCountCompleteOrder(db, de);
                user = reportMapper.queryCountUser(db, de);
                tOrder = reportMapper.queryCountOrder(db, de);

                t = turnover==null ? 0.0 : turnover.doubleValue();
                o = order==null ? 0 : order;
                u = user==null ? 0 : user;

                r = tOrder==null ? 1 :  (double)o/(double)tOrder;
                at = o==0 ? 0.0 : t/(double)o;

                row = sheet.getRow(7+i);

                row.getCell(1).setCellValue(now.minusDays(1+i)+"");
                row.getCell(2).setCellValue(t+"");
                row.getCell(3).setCellValue(o+"");
                row.getCell(4).setCellValue((r*100)+"%");
                row.getCell(5).setCellValue(at+"");
                row.getCell(6).setCellValue(u+"");

            }
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            sheets.write(outputStream);

            sheets.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            resourceAsStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
