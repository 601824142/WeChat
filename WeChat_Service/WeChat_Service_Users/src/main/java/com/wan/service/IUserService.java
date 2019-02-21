package com.wan.service;

import com.wan.pojo.User;

/**
 * @Author 万星明
 * @Date 2019/2/15
 */
public interface IUserService {

    //注册
    int register(User user);
    //登陆
    User login(String username, String password, String uuid);

    //修改用户头像到数据库
    int updateUserHeader(String header, String headerCrm, Integer userId);

    //通过用户名查询用户
    User searchUserByUserName(String username);

    //通过用户ID查询用户信息
    User queryUserByUserId(int id);

}
