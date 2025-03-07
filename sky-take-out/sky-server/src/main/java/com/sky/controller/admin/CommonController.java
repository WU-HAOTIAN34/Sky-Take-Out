package com.sky.controller.admin;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;



@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("upload")
    public Result<String> uploadFill(MultipartFile file){
        log.info("文件上传：{}", file);

        String[] temp = file.getOriginalFilename().split("\\.");
        String ex = temp[temp.length-1];
        String s = UUID.randomUUID().toString()+"."+ex;
        try {
            String res = aliOssUtil.upload(file.getBytes(), s);
            return Result.success(res);
        } catch (IOException e) {
            log.error("上传失败：{}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }


    }
}
