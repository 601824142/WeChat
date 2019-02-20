package com.wan.util;

/**
 * @Author 万星明
 * @Date 2019/2/15
 *
 * 状态码返回值
 */
public interface Constact {

    //成功
    String SUCCESS_CODE = "0000";
    //用户名已经存在
    String USERNAME_ARLEADE_HAVE_CODE = "0001";
    //用户名或密码异常
    String USERNAME_PASSWORD_ERROR_CODE="0002";
    //图片上传错误
    String UPLOAD_IMAGE_ERROR_CODE="0003";
    //其他错误
    String ERROR_CODE = "0004";

}
