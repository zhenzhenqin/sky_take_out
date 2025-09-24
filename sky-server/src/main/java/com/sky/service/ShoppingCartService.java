package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 清空购物车
     */
    void clean();

    /**
     * 购物车数量减一
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
