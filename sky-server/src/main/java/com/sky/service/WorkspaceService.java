package com.sky.service;

import com.sky.vo.BusinessDataVO;

public interface WorkspaceService {

    /**
     * 获得今日运营数据
     * @return
     */
    BusinessDataVO getBusinessData();
}
