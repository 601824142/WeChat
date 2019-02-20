package com.wan.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wan.dao.IFriendsDao;
import com.wan.fegin.UserFegin;
import com.wan.pojo.Friends;
import com.wan.pojo.User;
import com.wan.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author 万星明
 * @Date 2019/2/19
 */

@Service
public class FriendServiceImpl implements IFriendService {

    @Autowired
    private IFriendsDao friendsDao;

    @Autowired
    private UserFegin userFegin;


    /**
     * 判断两个人是否是好友关系
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public boolean isFriends(int userId, int friendId) {
        //新建查询条件
        QueryWrapper<Friends> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("friend_id",friendId);
        wrapper.ne("status",2);

        //直接查询记录,更快
        Integer count = friendsDao.selectCount(wrapper);
        //如果记录数量大于0,返回true,否则返回False
        return count > 0 ? true : false;
    }


    /**
     * 添加好友功能
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public int insertFriend(int userId, int friendId) {
        //新建一个好友对象
        Friends friends = new Friends();
        friends.setUserId(userId);
        friends.setFriendId(friendId);
        friends.setCtime(new Date());
        friends.setStatus(0);

        //判断是否是好友关系,如果不是,添加好友
        if (!isFriends(userId,friendId)){
            friendsDao.insert(friends);
        }

        //同时反过来,对方也要添加自己为好友
        if (!isFriends(friendId,userId)){
            friends.setUserId(friendId);
            friends.setFriendId(userId);
            friendsDao.insert(friends);
        }

        return 1;
    }


    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    @Override
    public List<Friends> listByUserId(int userId) {
        //新建查询条件,根据传进来的用户ID,查询其好友
        QueryWrapper<Friends> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.ne("status",2);//2表示该好友为黑名单

        //查询好友集合
        List<Friends> friends = friendsDao.selectList(wrapper);

        //遍历好友集合,填充好友的详细信息
        for (Friends friend : friends) {
            //根据好友ID查询具体信息
            User user = userFegin.queryUserById(friend.getFriendId());
            //将查询出来的用户存入好友类的Friend字段
            friend.setFriend(user);
        }
        //将好友集合返回
        return friends;
    }


}
