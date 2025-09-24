package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     */
    @ApiOperation("根据分类id查询套餐")
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("查询套餐的id是:{}", categoryId);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(StatusConstant.ENABLE);//将状态设置为起售状态
        setmeal.setCategoryId(categoryId);
        List<Setmeal> list =  setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * 根据套餐id获取里面的菜品详情
     */
    @ApiOperation("根据套餐id查询包含的菜品")
    @GetMapping("/dish/{id}")
    public Result<List<DishVO>> getDishBySetmealId(@PathVariable Long id){
        log.info("查询的套餐id是: {}", id);
        List<DishVO> list = setmealService.getDishBySetmealId(id);
        return Result.success(list);
    }
}
