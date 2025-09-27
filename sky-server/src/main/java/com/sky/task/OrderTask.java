package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 定时处理订单任务
 */
@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    //对于超时未支付订单 采取每分钟检测一次 如果超时 则关闭订单
    @Scheduled(cron = "0 * * * * *")
    public void processTimeoutOrder() {
        log.info("处理超时订单:{}", LocalDateTime.now());

        //查询超时订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getOrdersByStatusAndTime(Orders.PENDING_PAYMENT, time);

        //遍历数组 将订单取消
        for (Orders order : ordersList) {
            order.setStatus(Orders.CANCELLED);
            order.setCancelReason("订单超时未支付");
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }

    //对于派送中的订单， 采取每天凌晨1点检测一次，对于还在派送中的订单更新为已完成
    @Scheduled(cron = "0 0 1 * * *")
    public void processDeliveryOrder() {
        log.info("处理派送中的订单:{}", LocalDateTime.now());

        //查询所有派送中订单
        LocalDateTime time = LocalDateTime.now().plusHours(-1); //当前时间加上-1小时
        List<Orders> ordersList = orderMapper.getOrdersByStatusAndTime(Orders.DELIVERY_IN_PROGRESS, time);

        for (Orders order : ordersList){
            order.setStatus(Orders.COMPLETED);
            orderMapper.update(order);
        }
    }
}
