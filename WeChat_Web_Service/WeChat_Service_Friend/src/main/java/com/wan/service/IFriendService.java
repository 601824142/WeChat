package com.wan.service;

import com.wan.pojo.Friends;

import java.util.List;

/**
 * @Author 万星明
 * @Date 2019/2/18
 */
public interface IFriendService {

    //判断两个用户是否是好友关系
    boolean isFriends(int userId,int friendId);

    //插入一条好友关系
    int insertFriend(int userId,int friendId);

    //通过用户ID查询全部的好友对象
    List<Friends> listByUserId(int userId);

}
