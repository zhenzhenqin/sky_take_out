package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 套餐条件分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    List<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);


    /**
     * 保存套餐信息
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

}
