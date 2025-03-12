package com.sky.controller.user;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("user get category")
    public Result<List<Category>> getCategory(Integer type){
        log.info("查询分类");
        List<Category> res = categoryService.queryType(type);
        return Result.success(res);
    }

}
