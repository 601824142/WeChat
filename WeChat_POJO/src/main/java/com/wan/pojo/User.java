package com.wan.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 万星明
 * @Date 2019/2/15
 *
 * 用户类
 */
@Data
public class User implements Serializable {

    private int id;
    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nickname;
    //头像图片路径
    private String header;
    //头像缩略图路径
    private String headerCrm;
    //排序拼音
    private String pinyin;
    //二维码路径
    private String qrCode;
    //状态
    private int status;


}
