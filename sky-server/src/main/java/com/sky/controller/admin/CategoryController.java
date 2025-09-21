package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询
     * @param CategoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO CategoryPageQueryDTO){
        log.info("分页查询:{}", CategoryPageQueryDTO);
        PageResult page = categoryService.page(CategoryPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     * @return
     */
    @ApiOperation("启用、禁用分类")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("启用、禁用分类：{},{}",status, id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类:{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

}
