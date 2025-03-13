package com.sky.controller.user;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("save")
    public Result save(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("get user shopping cart")
    public Result<List<ShoppingCart>> getUserShoppingCart(){
        log.info("查看购物车");
        List<ShoppingCart> userShoppingCart = shoppingCartService.getUserShoppingCart(BaseContext.getCurrentId());
        return Result.success(userShoppingCart);
    }

    @DeleteMapping("/clean")
    @ApiOperation("clean")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean(BaseContext.getCurrentId());
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("reduce shopping cart")
    public Result reduce(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("减少购物车：{}", shoppingCartDTO);
        shoppingCartService.reduce(shoppingCartDTO);
        return Result.success();
    }

}
