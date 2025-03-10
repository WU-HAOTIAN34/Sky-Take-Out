package com.sky.controller.admin;
import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;


    @GetMapping("/status")
    @ApiOperation("getStatus")
    public Result<Integer> getStatus(){
        log.info("获取营业状态");
        Integer status = shopService.getStatus();
        return Result.success(status);
    }


    @PutMapping("/{status}")
    @ApiOperation("setStatus")
    public Result setStatus(@PathVariable Integer status){
        log.info("修改营业状态：{}", status);
        shopService.setStatus(status);
        return Result.success();
    }

}
