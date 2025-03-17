package com.sky.controller.user;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    @GetMapping("/list")
    @ApiOperation("list")
    public Result<List<AddressBook>> getUserAddress(){
        log.info("查询地址");
        List<AddressBook> address = addressBookService.getAddress(BaseContext.getCurrentId());
        return Result.success(address);
    }

    @PostMapping("")
    @ApiOperation("save")
    public Result save(@RequestBody AddressBook addressBook){
        log.info("新增地址：{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("get by id")
    public Result<AddressBook> getById(@PathVariable Long id){
        log.info("查询地址: {}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }


    @PutMapping("/default")
    @ApiOperation("set default")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }


    @PutMapping
    @ApiOperation("")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    @GetMapping("default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {


        List<AddressBook> list = addressBookService.getDefaultAddress(BaseContext.getCurrentId());

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }

}
