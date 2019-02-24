package com.wan.fegin;

import com.wan.pojo.WebSocketMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author 万星明
 * @Date 2019/2/22
 */
@FeignClient("WEB-SERVICE-CHAT")
public interface ChatFegin {

    @RequestMapping("/chat/sendMessage")
    void sendMessage(@RequestBody WebSocketMessage webSocketMessage);

}
