package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            Double turnover = orderMapper.getByDateAndStatus(map);

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
}
