package com.wan.service;

import com.wan.pojo.FriendRequest;

import java.util.List;

/**
 * @Author 万星明
 * @Date 2019/2/18
 */
public interface IFriendRequestService {

    //添加好友申请
    int insertFriendRequest(FriendRequest friendRequest);

    //根据被申请者,查询对应的
    List<FriendRequest> queryFriendRequest(int responseId);

    //用户处理好友申请
    int friendRequestHandler(int rid, int status);


}
