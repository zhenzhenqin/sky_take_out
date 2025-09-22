package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private FlavorMapper flavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    @Transactional //开启事务
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //插入一条菜品
        dishMapper.insert(dish);

        Long dishId = dish.getId();

        //插入n条口味
        if (dishDTO.getFlavors() != null && dishDTO.getFlavors().size() > 0) {
            //遍历数组插入菜品id
            List<DishFlavor> flavors = dishDTO.getFlavors();
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });

            flavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(@RequestParam List<Long> ids) {
        //判断当前菜品是否在起售中
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断当前菜品是否关联了套餐
        //根据菜品id获取套餐id
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            //当前菜品关联套餐 无法删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品数据
        dishMapper.deleteById(ids);

        //删除口味数据
        flavorMapper.deleteByDishId(ids);
    }

    /**
     * 根据id查询菜品数据和对应的口味数据
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavors(Long id) {
        //查菜品
        Dish dish = dishMapper.getById(id);

        //查id对应的口味数组
        List<DishFlavor> dishFlavors = flavorMapper.getByDishId(id);

        //将菜品和对应的口味封装到DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    @Transactional
    @Override
    public void updateWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //修改菜品
        dishMapper.updateDish(dish);

        //对于口味数据处理，进行先全部删除再插入
        //a.先进行全部删除
        flavorMapper.deleteByDishId(Collections.singletonList(dishDTO.getId()));

        //b.然后进行全部插入
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            //遍历插入数组
            flavors.forEach(flavor -> {
                flavor.setDishId(dish.getId());
            });
            //批量插入
            flavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 菜品状态修改
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //根据id修改状态
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.updateDish(dish);
    }

    /**
     *  根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        List<Dish> dishList = dishMapper.getByCategoryId(categoryId);

        return dishList;
    }

}
