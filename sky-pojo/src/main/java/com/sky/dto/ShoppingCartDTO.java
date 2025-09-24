package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId; //菜品id
    private Long setmealId; //套餐id
    private String dishFlavor; //菜品的口味

}
