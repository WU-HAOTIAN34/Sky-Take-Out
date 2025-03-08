package com.sky.controller.admin;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;



@RestController
@RequestMapping("admin/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @PostMapping("")
    @ApiOperation("save")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}", setmealDTO);
        setMealService.save(setmealDTO);
        return Result.success();
    }


    @GetMapping("page")
    @ApiOperation("page")
    public Result<PageResult> query(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("查询套餐：{}", setmealPageQueryDTO);
        PageResult pageResult = setMealService.query(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    @GetMapping("/{id}")
    @ApiOperation("get")
    public Result<SetmealVO> queryById(@PathVariable Long id){
        log.info("查询套餐：{}", id);
        SetmealVO setmealVO = setMealService.queryById(id);
        return Result.success(setmealVO);
    }

    @PutMapping("")
    @ApiOperation("edit")
    public Result edit(@RequestBody SetmealDTO setmealDTO){
        log.info("更新套餐：{}", setmealDTO);
        setMealService.edit(setmealDTO);
        return Result.success();
    }

    @DeleteMapping("")
    @ApiOperation("delete")
    public Result delete(@RequestParam List<Long>ids){
        log.info("删除菜品：{}", ids);
        setMealService.delete(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("status")
    public Result modifyStatus(@PathVariable Integer status, Long id){
        log.info("修改状态：{}", id);
        setMealService.modifyStatus(status, id);
        return Result.success();
    }


}
