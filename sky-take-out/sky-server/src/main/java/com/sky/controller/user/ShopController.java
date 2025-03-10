package com.sky.controller.user;
import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController("userShopController")
@RequestMapping("/user/shop")
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


}
