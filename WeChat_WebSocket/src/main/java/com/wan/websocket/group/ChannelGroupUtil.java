package com.wan.websocket.group;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 万星明
 * @Date 2019/2/22
 */

@Component
public class ChannelGroupUtil {

    //线程安全的Map(存储设备唯一标识码和通信管道)
    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 将设备标识和通信管道组成键值对存进Map
     * @param deviceId
     * @param channel
     * @return
     */
    public Channel put(String deviceId,Channel channel){
        return channelMap.put(deviceId,channel);
    }


    /**
     * 返回Map的长度
     * @return
     */
    public int size(){
        return channelMap.size();
    }


    /**
     * 根据设备标识获取通信管道
     * @param deviceId
     * @return
     */
    public Channel get(String deviceId){
        return channelMap.get(deviceId);
    }


    /**
     * 根据设备标识删除通信管道
     * @param deviceId
     * @return
     */
    public Channel removeByDeviceId(String deviceId){
        return channelMap.remove(deviceId);
    }


    /**
     * 根据通信链路删除键值对
     * @param channel
     * @return
     */
    public boolean removeByChannel(Channel channel){
        Set<Map.Entry<String, Channel>> entries = channelMap.entrySet();

        for (Map.Entry<String,Channel> entry:new HashSet<>((entries))){
            if (entry.getValue() == channel){
                return  entries.remove(entry.getKey());
            }
        }

        return false;
    }



}
