package com.sky.controller.admin;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    @ApiOperation("save")
    public Result save(@RequestBody CategoryDTO categoryDTO){

        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("query")
    public Result<PageResult> query(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("展示分类");
        PageResult pageResult = categoryService.query(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PutMapping("")
    @ApiOperation("modify")
    public Result modify(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类：{}", categoryDTO);
        categoryService.modify(categoryDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("status")
    public Result editStatus(@PathVariable Integer status, Long id){
        log.info("启用禁用分类：{}", id);
        categoryService.editStatus(status, id);
        return Result.success();
    }


    @DeleteMapping("")
    @ApiOperation("delete")
    public Result delete(Long id){
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("list")
    public Result<List<Category>> queryType(Integer type){
        List<Category> res = categoryService.queryType(type);
        return Result.success(res);
    }


}
