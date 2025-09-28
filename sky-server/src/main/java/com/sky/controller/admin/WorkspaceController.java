package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
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
}
