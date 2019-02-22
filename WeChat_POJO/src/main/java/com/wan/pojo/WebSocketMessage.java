package com.wan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author 万星明
 * @Date 2019/2/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage<T> implements Serializable {

    //请求ID
    private int requestId;
    //回应ID
    private int responseId;

    /**
     * 1 - 连接请求
     * 2 - 心跳消息
     */
    //数据类型(100 - 强制设备下线、101 - 有人申请添加好友)
    private int type;

    //硬件设备ID
    private String deviceId;

    //消息格式(String、视频、音频)
    private T Content;


}
