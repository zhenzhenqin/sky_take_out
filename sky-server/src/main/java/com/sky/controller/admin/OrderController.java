package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Api("订单相关接口")
@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     * @return
     */
    @ApiOperation("订单搜索")
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索参数:{}", ordersPageQueryDTO);
        PageResult pageResult =  orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @ApiOperation("根据id获取订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> getDetailById(@PathVariable Long id){
        log.info("订单详情id:{}", id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 各个状态订单数量统计
     * @return
     */
    @ApiOperation("获取订单统计信息")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics(){
        log.info("获取订单统计信息");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }
}
