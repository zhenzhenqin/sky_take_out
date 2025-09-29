package com.sky.service.impl;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WorkspaceServiceImpl workspaceService;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //计算时间范围
        List<LocalDate> dataList = new ArrayList<>();
        dataList.add(begin);
        //只要begin小于end，就循环加一天然后存入时间list中 直到begin等于end为止
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dataList.add(begin);
        }
        //将时间列表转化为逗号分隔的字符串
        String dateListStr = StringUtils.join(dataList, ',');

        //遍历时间列表  根据具体时间获取当日营业额
        List<Double> turnoverList = new ArrayList<>(); //数组存放营业额
        for (LocalDate date : dataList){
            //根据时间以及订单状态为已完成来查询营业额的总和
            //获取当日时间的最小值和最大值
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //将查询数据封装进入map中
            Map map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.getByDateAndStatus(map); //获取当日营业额

            //如果当日营业额为空的话 则将营业额设置为0.0
            if(turnover == null){
                turnover = 0.0;
            }

            turnoverList.add(turnover);
        }

        //将营业额列表转化为逗号分隔的字符串
        String turnoverListStr = StringUtils.join(turnoverList, ',');

        //封装vo对象返回前端
        return TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(turnoverListStr)
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //处理时间
        List<LocalDate> dataList = new ArrayList<>();
        dataList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dataList.add(begin);
        }

        //将时间列表转化为逗号分隔的字符串
        String dateListStr = StringUtils.join(dataList, ',');

        //处理用户总量
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate date : dataList){
            //根据时间查询用户数量
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map1 = new HashMap<>();

            Map map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);

            //获取总用户数
            Integer totalUser = userMapper.getUserByDateAndStatus(map1);
            if (totalUser == null){
                totalUser = 0;
            }
            totalUserList.add(totalUser);

            //获取当日新增用户数
            Integer newUser = userMapper.getUserByDateAndStatus(map);
            if (newUser == null){
                newUser = 0;
            }
            newUserList.add(newUser);
        }

        String totalUserListStr = StringUtils.join(totalUserList, ',');
        String newUserListStr = StringUtils.join(newUserList, ',');


        return UserReportVO.builder()
                .dateList(dateListStr)
                .totalUserList(totalUserListStr)
                .newUserList(newUserListStr)
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getUserOrdersStatistics(LocalDate begin, LocalDate end) {

        //获取总时间最小和最大时间 用于统计全局
        LocalDateTime beginTime1 = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime1 = LocalDateTime.of(end, LocalTime.MAX);

        Map map1 = new HashMap();
        map1.put("beginTime",beginTime1);
        map1.put("endTime",endTime1);

        //转换时间
        List<LocalDate> dataList = new ArrayList<>();
        dataList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dataList.add(begin);
        }

        String dateListStr = StringUtils.join(dataList, ",");

        //存放总订单数列表
        List<Integer> allOrderCountList = new ArrayList<>();
        //存放有效订单数列表
        List<Integer> validOrderCountList = new ArrayList<>();

        //遍历时间 根据时间获取相应数据
        for(LocalDate date : dataList){
            //获取当日时间的最小值和最大值
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //获取当日订单数列表 根据时间去查询订单总数
            Map map = new HashMap<>();
            map.put("beginTime",beginTime);
            map.put("endTime",endTime);
            Integer orderCount = orderMapper.getOrdersNumberByDateAndStatus(map); //获取订单总数
            if(orderCount == null){
                orderCount = 0;
            }

            allOrderCountList.add(orderCount);

            //获取当日有效订单列表  根据时间去查询有效订单数
            map.put("status",Orders.COMPLETED);
            Integer validOrderCount = orderMapper.getOrdersNumberByDateAndStatus(map); //获取有效订单数
            if(validOrderCount == null){
                validOrderCount = 0;
            }
            validOrderCountList.add(validOrderCount);
        }

        //获取时间区间内的订单总数
        Integer allOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map1);
        if (allOrdersNumber == null){
            allOrdersNumber = 0;
        }

        //获取时间区间内的有效订单数
        map1.put("status",Orders.COMPLETED);
        Integer validOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map1);
        if (validOrdersNumber == null){
            validOrdersNumber = 0;
        }

        //获取时间区间内的订单完成率  有效订单数/订单总数
        Double orderCompletionRate = validOrdersNumber.doubleValue() / allOrdersNumber;

        String allOrderCountListStr = StringUtils.join(allOrderCountList, ',');
        String validOrderCountListStr = StringUtils.join(validOrderCountList, ',');

        return OrderReportVO.builder()
                .dateList(dateListStr)
                .orderCompletionRate(orderCompletionRate)
                .orderCountList(allOrderCountListStr)
                .totalOrderCount(allOrdersNumber)
                .validOrderCount(validOrdersNumber)
                .validOrderCountList(validOrderCountListStr)
                .build();
    }

    /**
     * 销量排名Top10
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        //返回商品名称列表
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        //根据时间查询销量排名Top10
        Map map = new HashMap<>();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        List<OrderDetail> salesTop10OrderDetailList = orderDetailMapper.getSalesTop10(map);

        List<String> numberList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        for (OrderDetail orderDetail: salesTop10OrderDetailList){
            numberList.add(String.valueOf(orderDetail.getNumber()));
            nameList.add(orderDetail.getName());
        }

        String nameListStr = StringUtils.join(nameList, ',');
        String numberListStr = StringUtils.join(numberList, ',');

        return SalesTop10ReportVO.builder()
                .nameList(nameListStr)
                .numberList(numberListStr)
                .build();
    }

    /**
     *  导出运营数据报表
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData30(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData30(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
