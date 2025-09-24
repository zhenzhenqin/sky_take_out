package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId()); //设置用户id

        //从数据库表中查询是否存在该商品
        ShoppingCart cart = shoppingCartMapper.getByUserIdAndDishId(shoppingCart);
        if(cart != null){
            //数据库存在此商品，商品加1
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.update(cart);
        } else{
            //不存在 则插入购物车

            //获取菜品id和套餐id判断当前商品是菜品还是套餐
            Long dishId = shoppingCart.getDishId();
            Long setmealId = shoppingCart.getSetmealId();

            if(dishId != null){
                //当前商品是菜品

                //查询菜品信息
                Dish dish = dishMapper.getById(dishId);
                //存入数据
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else {
                //当前商品是套餐
                Setmeal setmeal = setmealMapper.getSetmealById(setmealId);

                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }

            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 购物车数量减一
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        //设置查询条件 判断是否为空
        ShoppingCart cart = shoppingCartMapper.getByUserIdAndDishId(shoppingCart);
        if(cart != null){
            //如果商品为1，则删除
            if(cart.getNumber() == 1){
                shoppingCartMapper.deleteById(cart.getId());
            }else{
                //不为1 则减少1后更新
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.update(cart);
            }

        }
    }
}
