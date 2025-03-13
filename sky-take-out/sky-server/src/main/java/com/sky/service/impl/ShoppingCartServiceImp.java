package com.sky.service.impl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class ShoppingCartServiceImp implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private DishMapper dishMapper;

    public void add(ShoppingCartDTO shoppingCartDTO){
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart s = shoppingCartMapper.queryByUserIdDishSetMealID(shoppingCart);
        if (s != null){
            s.setNumber(s.getNumber()+1);
            shoppingCartMapper.updateNum(s);
            return;
        }
        if (shoppingCart.getDishId() != null){
            DishVO dishVO = dishMapper.queryById(shoppingCart.getDishId());
            shoppingCart.setImage(dishVO.getImage());
            shoppingCart.setName(dishVO.getName());
            shoppingCart.setAmount(dishVO.getPrice());
        }else{
            SetmealVO setmealVO = setMealMapper.queryById(shoppingCart.getSetmealId());
            shoppingCart.setImage(setmealVO.getImage());
            shoppingCart.setName(setmealVO.getName());
            shoppingCart.setAmount(setmealVO.getPrice());
        }
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());

        shoppingCartMapper.save(shoppingCart);

    }


    public List<ShoppingCart> getUserShoppingCart(Long id){
        return shoppingCartMapper.queryShoppingCartByUserId(id);
    }

    public void clean(Long id){
        shoppingCartMapper.clean(id);
    }

    public void reduce(ShoppingCartDTO shoppingCartDTO){
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart shoppingCart1 = shoppingCartMapper.queryByUserIdDishSetMealID(shoppingCart);
        if (shoppingCart1.getNumber() != 1){
            shoppingCart1.setNumber(shoppingCart1.getNumber()-1);
            shoppingCartMapper.updateNum(shoppingCart1);
        }else{
            shoppingCartMapper.deleteById(shoppingCart1.getId());
        }

    }


}
