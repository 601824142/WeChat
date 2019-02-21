package com.wan.controller;

import com.wan.pojo.FriendRequest;
import com.wan.pojo.Friends;
import com.wan.service.IFriendRequestService;
import com.wan.service.IFriendService;
import com.wan.util.Constact;
import com.wan.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 万星明
 * @Date 2019/2/19
 */
@RestController
@RequestMapping("/friend")
public class FriendsController {

    @Autowired
    private IFriendService friendService;

    @Autowired
    private IFriendRequestService friendRequestService;


    /**
     * 添加好友申请记录
     * @param friendRequest
     * @return
     */
    @RequestMapping("/addFriendRequest")
    public ResultData<Boolean> addFriendRequest(FriendRequest friendRequest){
        //调用服务添加申请
        int result  = friendRequestService.insertFriendRequest(friendRequest);

        //如果大于0—添加成功,等于-1—申请过了(无需重复申请),等于-2(已经是好友关系)
        if (result > 0){
            return ResultData.createResultData(true);
        }else if(result == -1){
            return ResultData.createResultData(Constact.ERROR_CODE,"请不要重复申请！");
        }else if(result == -2){
            return ResultData.createResultData(Constact.ERROR_CODE,"对方已经是你的好友！");
        }

        //其他问题返回服务器异常
        return ResultData.createResultData(Constact.ERROR_CODE,"服务器已经爆炸！！");
    }


    /**
     * 根据用户信息,查询发送给该用户的所有好友申请
     * @param responseId
     * @return
     */
    @RequestMapping("/queryFriendRequest")
    public ResultData<List<FriendRequest>> queryFriendRequest(int responseId){
        //调用服务,查询申请记录集合
        List<FriendRequest> friendRequests = friendRequestService.queryFriendRequest(responseId);
        //如果集合不为空,则将申请记录集合返回
        if (friendRequests != null && friendRequests.size() > 0 ){
            return ResultData.createResultData(friendRequests);
        }
        //否则返回错误信息
        return ResultData.createResultData(Constact.ERROR_CODE,"无被添加记录！");
    }


    /**
     * 好友申请记录的处理
     * @param rid
     * @param status
     * @return
     */
    @RequestMapping("/friendRequestHandler")
    public ResultData<Boolean> friendRequestHandler(int rid,int status){
        int handler = friendRequestService.friendRequestHandler(rid, status);
        //返回处理成功
        return ResultData.createResultData(true);
    }


    /**
     * 根据用户Id查询好友列表
     * @param userId
     * @return
     */
    @RequestMapping("/queryFriendsByUserId")
    public ResultData<List<Friends>> queryFriendsByUserId(int userId){
        List<Friends> friends = friendService.listByUserId(userId);
        //返回好友列表集合
        return ResultData.createResultData(friends);
    }


}
