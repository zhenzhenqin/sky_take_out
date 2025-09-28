package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkspaceService {

    /**
     * 获得今日运营数据
     * @return
     */
    BusinessDataVO getBusinessData();

    /**
     * 获得订单统计数据
     * @return
     */
    OrderOverViewVO getOrderOverView();


    /**
     * 获得菜品统计数据
     * @return
     */
    DishOverViewVO getDishOverView();


    /**
     * 获得套餐统计数据
     * @return
     */
    SetmealOverViewVO getSetmealOverView();
}
