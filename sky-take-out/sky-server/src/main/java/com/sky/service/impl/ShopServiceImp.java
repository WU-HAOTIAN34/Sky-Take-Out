package com.sky.service.impl;
import com.sky.constant.RedisKeyConstant;
import com.sky.constant.StatusConstant;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.EOFException;



@Service
public class ShopServiceImp implements ShopService {

    @Autowired
    private RedisTemplate redisTemplate;

    public Integer getStatus() {

        Integer res;
        try{
            res = (Integer)redisTemplate.opsForValue().get(RedisKeyConstant.SHOP_STATUS);
            return res;
        }catch (Exception e){
            redisTemplate.opsForValue().set(RedisKeyConstant.SHOP_STATUS, StatusConstant.DISABLE);
            return StatusConstant.DISABLE;
        }
    }

    public void setStatus(Integer status) {
        redisTemplate.opsForValue().set(RedisKeyConstant.SHOP_STATUS, status);
    }
}
