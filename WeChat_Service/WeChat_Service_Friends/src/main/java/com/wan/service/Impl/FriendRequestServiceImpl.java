package com.wan.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wan.dao.IFriendRequestDao;
import com.wan.dao.IFriendsDao;
import com.wan.fegin.ChatFegin;
import com.wan.fegin.UserFegin;
import com.wan.pojo.FriendRequest;
import com.wan.pojo.User;
import com.wan.pojo.WebSocketMessage;
import com.wan.service.IFriendRequestService;
import com.wan.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 万星明
 * @Date 2019/2/18
 */
@Service
public class FriendRequestServiceImpl implements IFriendRequestService {

        @Autowired
        private IFriendRequestDao friendRequestDao;

        @Autowired
        private UserFegin userFegin;

        @Autowired
        private IFriendService friendService;

        @Autowired
        private IFriendsDao friendsDao;

        @Autowired
        private ChatFegin chatFegin;


        /**
         * 申请好友,添加申请好友记录
         * @param friendRequest
         * @return
         */
        @Override
        public int insertFriendRequest(FriendRequest friendRequest) {
            //判断当前用户是否添加过好友申请
            QueryWrapper<FriendRequest> wrapper = new QueryWrapper<>();
            wrapper.eq("request_id",friendRequest.getRequestId());
            wrapper.eq("response_id",friendRequest.getResponseId());
            wrapper.eq("status",0);

            //查询相应的申请记录是否存在,直接查询记录数量
            Integer count = friendRequestDao.selectCount(wrapper);

            //数据库中存在申请记录,还没处理
            if (count >0 ){
                return -1;
            }

            //调用好友接口,查询两人是否已经是好友关系
            if (friendService.isFriends(friendRequest.getRequestId(),friendRequest.getResponseId())){
                return -2;//如果已经是好友关系,返回-2
            }

            //发送消息通知被申请人,有人添加他为好友
            int responseId = friendRequest.getResponseId();
            WebSocketMessage message = new WebSocketMessage(friendRequest.getRequestId(), responseId, 101, null, null);
            //发送消息
            chatFegin.sendMessage(message);


            //如果既没有未处理的申请记录,又不是好友关系,则插入一条好友申请记录
            return friendRequestDao.insert(friendRequest);
        }


        /**
         * 查询当前用户的被申请好友记录
         * @param responseId
         * @return
         */
        @Override
        public List<FriendRequest> queryFriendRequest(int responseId) {
            //新建查询条件
            QueryWrapper<FriendRequest> wrapper = new QueryWrapper<>();
            wrapper.eq("response_id",responseId);
            //通过申请时间倒序排列
            wrapper.orderByDesc("stime");

            //调用Dao层方法查询相应的好友申请记录集合
            List<FriendRequest> friendRequestList = friendRequestDao.selectList(wrapper);

            //遍历申请记录,将相应申请人的具体信息查询出来
            for (FriendRequest friendRequest : friendRequestList) {
                //查询信息
                User user = userFegin.queryUserById(friendRequest.getRequestId());
                //将查询出来的用户信息存入到请求对象的用户属性中(数据库不存在该字段)
                friendRequest.setRequestUser(user);
            }

            return friendRequestList;
        }


        /**
         * 用户处理好友申请
         * @param rid
         * @param status
         * @return
         */
        @Override
        public int friendRequestHandler(int rid, int status) {
            //通过申请记录ID,查询申请记录
            FriendRequest friendRequest = friendRequestDao.selectById(rid);
            //修改其状态(状态为传进来的值)
            friendRequest.setStatus(status);
            //数据库中,更新该申请记录的状态
            int result = friendRequestDao.updateById(friendRequest);

            //如果更新的返回结果大于0,并且修改的状态为1,则调用好友接口,添加好友
            if (result >0 && status == 1){
                friendService.insertFriend(friendRequest.getRequestId(),friendRequest.getResponseId());
            }

            //调用完成
            return 1;
        }


}
