package com.sky.controller.admin;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;



@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    @GetMapping("/page")
    @ApiOperation("page")
    public Result<PageResult> query(DishPageQueryDTO dishPageQueryDTO){
        log.info("展示菜品");
        PageResult pageResult = dishService.query(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    @PostMapping("")
    @ApiOperation("save")
    @CacheEvict(cacheNames = "dish", key = "#dishDTO.categoryId")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }

    @DeleteMapping("")
    @ApiOperation("delete")
    @CacheEvict(cacheNames = "dish", allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除从菜品：{}", ids);
        dishService.delete(ids);
        return Result.success();
    }

    @PutMapping("")
    @ApiOperation("edit")
    @CacheEvict(cacheNames = "dish", allEntries = true)
    public Result edit(@RequestBody DishDTO dishDTO){
        log.info("修改菜品: {}", dishDTO);
        dishService.edit(dishDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("query")
    public Result<DishVO> queryById(@PathVariable Long id){
        log.info("获取菜品：{}", id);
        DishVO dishVO = dishService.queryById(id);
        return Result.success(dishVO);

    }

    @PostMapping("/status/{status}")
    @ApiOperation("status")
    @CacheEvict(cacheNames = "dish", allEntries = true)
    public Result modifyStatus(@PathVariable Integer status, Long id){
        log.info("启用禁用：{}", id);
        dishService.modifyStatus(status, id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("list")
    public Result<List<DishVO>> queryByCategoryId(Long categoryId){
        log.info("展示分类菜品：{}", categoryId);
        List<DishVO> list = dishService.queryByCategory(categoryId);
        return Result.success(list);
    }

}
