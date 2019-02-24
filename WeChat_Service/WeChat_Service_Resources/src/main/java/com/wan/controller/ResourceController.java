package com.wan.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wan.fegin.UserFegin;
import com.wan.pojo.User;
import com.wan.util.Constact;
import com.wan.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 万星明
 * @Date 2019/2/16
 */
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private UserFegin userFegin;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Value("${fdfs.serverip}")
    private String imgServerIp;

    /**
     * 图片上传服务
     * @param file
     * @param userId
     * @return
     */
    @RequestMapping("/uploadImg")
    public ResultData<Map> uploadImg(MultipartFile file,Integer userId){
        System.out.println("进入图片上传服务:"+file.getOriginalFilename()+","+file.getSize());
        log.debug("进入图片上传服务:"+file.getOriginalFilename()+","+file.getSize());
        try {
            //服务器上传图片
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), "PNG", null);

            //获得图片上传的路径
            String fullPath = storePath.getFullPath();
            //获得缩略图的路径
            String minFullPath = fullPath.replace(".", "_80x80.");

            //将两个地址装入到Map集合
            HashMap<String, String> map = new HashMap<>();
            map.put("header",imgServerIp+fullPath);
            map.put("headerCrm",imgServerIp+minFullPath);

            //将上传的图片路径保存到数据库中
            ResultData<Boolean> resultData = userFegin.updateUserHeader(fullPath, minFullPath, userId);
            if (resultData.getCode().equals("0000")){
                //返回上传成功的数据
                return ResultData.createResultData(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回错误结果
        return  ResultData.createResultData(Constact.UPLOAD_IMAGE_ERROR_CODE,"图片上传失败！");
    }

    @RequestMapping("/test")
    public User test(){
        User user = userFegin.queryUserById(7);
        return user;
    }


}
