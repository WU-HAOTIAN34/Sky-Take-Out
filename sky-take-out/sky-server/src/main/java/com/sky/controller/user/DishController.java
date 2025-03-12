package com.sky.controller.user;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    @GetMapping("/list")
    @ApiOperation("get dish by category id")
    public Result<List<DishVO>> getDish(Long categoryId){
        log.info("查询菜品：{}", categoryId);
        List<DishVO> list = dishService.queryByCategoryWithFlavor(categoryId);
        return Result.success(list);
    }

}
