package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用相关接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){

        log.info("文件上传开始");

        //获取原始文件名
        String originalFilename = file.getOriginalFilename();

        //获取文件拓展名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        //UUID生成随机铭记
        String objectName = UUID.randomUUID().toString() + extension;

        try {
            String url = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败,{}",e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
