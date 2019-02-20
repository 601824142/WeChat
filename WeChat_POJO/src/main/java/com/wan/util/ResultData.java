package com.wan.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 万星明
 * @Date 2019/2/15
 *
 * 返回给前端人员的结果数据包装类
 */

@Data
public class ResultData<T> implements Serializable {

    private String code; //状态码
    private String message; //简单的结果描述

    private T data; //包装好的泛型数据


    /**
     * 成功后,返回的泛型包装类对象
     * @param data (泛型数据类型对象)
     * @param <T>
     * @return
     */
    public static  <T> ResultData<T> createResultData(T data){
        //新建泛型包装类对象
        ResultData<T> resultData = new ResultData<>();
        //设置包装类的属性
        resultData.setCode(Constact.SUCCESS_CODE);
        resultData.setMessage("成功！");
        resultData.setData(data);
        //返回创建好的泛型包装类
        return resultData;
    }


    /**
     * 失败后,返回的泛型包装类对象
     * @param code (状态码)
     * @param message (返回失败的消息)
     * @param <T>
     * @return
     */
    public static  <T> ResultData<T> createResultData(String code,String message){
        //新建泛型包装类对象
        ResultData<T> resultData = new ResultData<>();
        //设置包装类的属性
        resultData.setCode(code);
        resultData.setMessage(message);
        //返回创建好的泛型包装类
        return resultData;
    }


}
