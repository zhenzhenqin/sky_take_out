package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台相关接口")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 获得今日运营数据
     * @return
     */
    @ApiOperation("获取营业数据")
    @GetMapping("/businessData")
    public Result<BusinessDataVO > getBusinessData(){
        log.info("获取营业数据");
        BusinessDataVO businessDataVO = workspaceService.getBusinessData();
        return Result.success(businessDataVO);
    }


    /**
     * 获得订单统计数据
     * @return
     */
    @ApiOperation("获取订单管理数据")
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOrderOverView(){
        log.info("获取订单统计数据");
        OrderOverViewVO orderOverViewVO = workspaceService.getOrderOverView();
        return Result.success(orderOverViewVO);
    }

    /**
     * 获得菜品总览数据
     * @return
     */
    @ApiOperation("获取菜品总览数据")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getDishOverView(){
        log.info("获取菜品总览数据");
        DishOverViewVO dishOverViewVO = workspaceService.getDishOverView();
        return Result.success(dishOverViewVO);
    }

    @ApiOperation("获取套餐总览数据")
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> getSetmealOverView(){
        log.info("获取套餐总览数据");
        SetmealOverViewVO setmealOverViewVO = workspaceService.getSetmealOverView();
        return Result.success(setmealOverViewVO);
    }
}
