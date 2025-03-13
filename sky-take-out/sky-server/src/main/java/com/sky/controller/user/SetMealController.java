package com.sky.controller.user;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;



@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @GetMapping("/list")
    @ApiOperation("get setmeal")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    public Result<List<SetmealVO>> getSetMeal(Long categoryId){
        log.info("查询套餐: {}", categoryId);
        List<SetmealVO> setmeals = setMealService.queryByCategoryId(categoryId);
        return Result.success(setmeals);
    }


    @GetMapping("/dish/{id}")
    @ApiOperation("get setmeal dish")
    public Result<List<DishItemVO>> getDishBySetMealId(@PathVariable Long id){
        log.info("查询套餐菜品：{}", id);
        List<DishItemVO> dishItemVOS = setMealService.queryDishBySetMealId(id);
        return Result.success(dishItemVOS);
    }

}
