package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
