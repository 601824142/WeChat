package com.wan.fegin;

import com.wan.pojo.User;
import com.wan.util.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author 万星明
 * @Date 2019/2/17
 */
@FeignClient("WEB-SERVICE-USER")
public interface UserFegin {

    /**
     * 修改用户头像功能的接口
     * @param header
     * @param headerCrm
     * @param userId
     * @return
     */

    @RequestMapping("/user/updateheader")
    ResultData<Boolean> updateUserHeader(
            @RequestParam("header") String header,
            @RequestParam("headerCrm") String headerCrm,
            @RequestParam("userId") Integer userId);


    @RequestMapping("/user/queryUserById")
    User queryUserById(@RequestParam("id") int id);


}
