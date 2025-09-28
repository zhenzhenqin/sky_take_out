package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获得今日运营数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {
        LocalDateTime beginTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Map map = new HashMap<>();
        map.put("status",Orders.COMPLETED);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        //获取新增用户数
        Map map1 = new HashMap<>(); //特殊情况 如不需要传入状态的情况
        map1.put("beginTime", beginTime);
        map1.put("endTime", endTime);
        Integer newUser = userMapper.getUserByDateAndStatus(map1);
        if (newUser == null){
            newUser = 0;
        }

        //获取有效订单数
        Integer validOrderCount = orderMapper.getOrdersNumberByDateAndStatus(map); //获取有效订单数
        if(validOrderCount == null){
            validOrderCount = 0;
        }

        //获取订单完成率
        Integer orderCount = orderMapper.getOrdersNumberByDateAndStatus(map1); //获取订单总数
        if(orderCount == null){
            orderCount = 0;
        }

        Double orderCompleteRate = validOrderCount / (double)orderCount;  //订单完成率


        //获取营业额
        Double turnover = orderMapper.getByDateAndStatus(map);  //获取当日营业额
        if(turnover == null){
            turnover = 0.0;
        }

        //获取平均客单价
        Double avgOrderPrice = turnover / validOrderCount;  //平均客单价


        //封装vo返回
        return BusinessDataVO.builder()
                .newUsers(newUser)
                .orderCompletionRate(orderCompleteRate)
                .turnover( turnover)
                .unitPrice(avgOrderPrice)
                .validOrderCount(validOrderCount)
                .build();
    }
}
