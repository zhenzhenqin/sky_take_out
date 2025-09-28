package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;

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
}
