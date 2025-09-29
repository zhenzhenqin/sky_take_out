package com.sky.service.impl;


import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
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
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    private final LocalDateTime beginTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    private final LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

    /**
     * 获得今日运营数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {

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
        Double orderCompleteRate = 0.0;
        if(orderCount != 0){
            orderCompleteRate = validOrderCount / (double)orderCount;//订单完成率
        }


        //获取营业额
        Double turnover = orderMapper.getByDateAndStatus(map);
        if(turnover == null){
            turnover = 0.0;
        }

        //获取平均客单价
        Double avgOrderPrice = 0.0;
        if(validOrderCount != 0){
            avgOrderPrice = turnover / validOrderCount;
        }


        //封装vo返回
        return BusinessDataVO.builder()
                .newUsers(newUser)
                .orderCompletionRate(orderCompleteRate)
                .turnover( turnover)
                .unitPrice(avgOrderPrice)
                .validOrderCount(validOrderCount)
                .build();
    }


    /**
     * 获得订单统计数据
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverView() {


        //对于没有订单状况查询的处理
        Map map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        //返回全部订单
        Integer allOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取订单总数
        if (allOrdersNumber == null){
            allOrdersNumber = 0;
        }

        //返回已取消数量
        map.put("status",Orders.CANCELLED);
        Integer cancelledOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取已取消订单数
        if (cancelledOrdersNumber == null){
            cancelledOrdersNumber = 0;
        }

        //返回已完成数量
        map.put("status",Orders.COMPLETED);
        Integer completedOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取已完成订单数
        if (completedOrdersNumber == null){
            completedOrdersNumber = 0;
        }

        //返回待派送数量
        map.put("status",Orders.DELIVERY_IN_PROGRESS);
        Integer deliveryInProgressOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取待派送订单数
        if (deliveryInProgressOrdersNumber == null){
            deliveryInProgressOrdersNumber = 0;
        }

        //返回待接单数量
        map.put("status",Orders.TO_BE_CONFIRMED);
        Integer pendingOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取待接单订单数
        if (pendingOrdersNumber == null){
            pendingOrdersNumber = 0;
        }

        //封装vo返回
        return OrderOverViewVO.builder()
                .allOrders(allOrdersNumber)
                .cancelledOrders(cancelledOrdersNumber)
                .completedOrders(completedOrdersNumber)
                .deliveredOrders(deliveryInProgressOrdersNumber)
                .waitingOrders(pendingOrdersNumber)
                .build();
    }


    /**
     * 获得菜品统计数据
     * @return
     */
    @Override
    public DishOverViewVO getDishOverView() {
        //已停售菜品数量
        Dish dish = new Dish();
        dish.setStatus(0);

        int stopNumber = dishMapper.list(dish).size();

        //已起售菜品数量
        dish.setStatus(1);
        int startNumber = dishMapper.list(dish).size();

        //封装vo返回
        return DishOverViewVO.builder()
                .discontinued(stopNumber)
                .sold(startNumber)
                .build();
    }

    /**
     * 获得套餐统计数据
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        //已停售套餐
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(0);
        int stopNumber = setmealMapper.list(setmeal).size();

        //已起售套餐数量
        setmeal.setStatus(1);
        int startNumber = setmealMapper.list(setmeal).size();

        //封装vo返回
        return SetmealOverViewVO.builder()
                .discontinued(stopNumber)
                .sold(startNumber)
                .build();
    }

    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData30(LocalDateTime begin, LocalDateTime end) {

        Map map = new HashMap<>();
        map.put("status",Orders.COMPLETED);
        map.put("beginTime", begin);
        map.put("endTime", end);

        //获取新增用户数
        Map map1 = new HashMap<>(); //特殊情况 如不需要传入状态的情况
        map1.put("beginTime", begin);
        map1.put("endTime", end);
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
        Double orderCompleteRate = 0.0;

        if(orderCount != 0){
            orderCompleteRate = validOrderCount / (double)orderCount; //订单完成率
        }

        //获取营业额
        Double turnover = orderMapper.getByDateAndStatus(map);  //获取当日营业额
        if(turnover == null){
            turnover = 0.0;
        }

        //获取平均客单价
        //获取平均客单价
        Double avgOrderPrice = 0.0;
        if(validOrderCount != 0){
            avgOrderPrice = turnover / validOrderCount;
        }


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
