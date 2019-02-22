package com.wan.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wan.dao.IUserDao;
import com.wan.fegin.ChatFegin;
import com.wan.pojo.User;
import com.wan.pojo.WebSocketMessage;
import com.wan.service.IUserService;
import com.wan.util.MD5Util;
import com.wan.util.PinyinUtil;
import com.wan.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author 万星明
 * @Date 2019/2/15
 */

@Service //直接用spring的注解,这不是dubbo
public class UserServiceImpl implements IUserService {

        @Autowired
        private IUserDao userDao;

        @Autowired
        private FastFileStorageClient fastFileStorageClient;

        //注入Redis
        @Autowired
        private RedisTemplate redisTemplate;

        @Autowired
        private ChatFegin chatFegin;

        /**
         * 注册用户
         * @param user
         * @return
         */
        @Override
        public int register(User user) {
                //设置查询条件,查询用户是否已经注册过
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                System.out.println("注册服务,用户名:"+user.getUsername());

                wrapper.eq("username",user.getUsername());
                User u = userDao.selectOne(wrapper);

                //如果已经存在
                if(u != null){
                        //用户名已经存在
                        return -1;
                }

                //对密码进行MD5加密
                user.setPassword(MD5Util.md5(user.getPassword()));

                //生成二维码临时文件—然后保存到FDFS中—最后删除
                File file = null;
                try {
                        //1、创建一个PNG格式的文件
                        file = File.createTempFile(user.getUsername() + "qrcode", "png");
                        //2、通过工具类创建二维码写入到PNG文件中,返回true/false
                        boolean flag = QRCodeUtil.createQRCode(file, "WeChat:" + user.getUsername());
                        //3、如果创建成功,将PNG文件上传到FDFS
                        if (flag){
                                StorePath png = fastFileStorageClient.uploadImageAndCrtThumbImage(new FileInputStream(file), file.length(), "PNG", null);
                                user.setQrCode(png.getFullPath());
                        }

                } catch (IOException e) {
                        e.printStackTrace();
                }finally {
                        //如果文件不为空,删除
                        if (file != null){
                                file.delete();
                        }
                }



                //生成昵称的拼音
                String pinyin = PinyinUtil.stringToPinyin(user.getNickname());
                user.setPinyin(pinyin.toUpperCase());


                //否则返回注册后的正值
                return userDao.insert(user);
        }


        /**
         * 用户登录
         * @param username
         * @param password
         * @param uuid (设备的唯一标识)
         * @return
         */
        @Override
        public User login(String username, String password,String uuid) {
                //设置查询条件
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("username",username);
                //密码已经被编码过,需要再解码
                wrapper.eq("password",MD5Util.md5(password));

                //登陆查询用户
                User user = userDao.selectOne(wrapper);

                //TODO 将登陆的用户和设备唯一标识绑定
                if (user != null){
                        //获得当前登陆的设备标识UUID
                        //获得当前登陆的用户ID
                        //将用户ID和设备标识UUID绑定,存放在Redis中
                        String oldDeviceId = (String) redisTemplate.opsForValue().get(user.getId());
                        //如果之前登陆的设备ID不为空,则通知旧设备下线
                        if (oldDeviceId != null){
                                //新建消息,将消息发送给通信微服务,然后通知旧设备下线
                                WebSocketMessage message = new WebSocketMessage(-1, -1, 100, oldDeviceId, null);
                                //通过调用聊天服务发送消息
                                chatFegin.sendMessage(message);

                        }
                        //将新登陆的设备和用户ID绑定
                        redisTemplate.opsForValue().set(user.getId(),uuid);
                }

                return user;
        }


        /**
         * 修改用户的头像到数据库中
         * @param header
         * @param headerCrm
         * @param userId
         * @return
         */
        @Override
        public int updateUserHeader(String header, String headerCrm, Integer userId) {
                //通过用户ID查询该用户
                User user = userDao.selectById(userId);
                //如果用户不为空
                if(user!=null){
                        user.setHeader(header);
                        user.setHeaderCrm(headerCrm);
                        return userDao.updateById(user);
                }
                return 0;
        }


        /**
         * 通过用户名查询用户
         * @param username
         * @return
         */
        @Override
        public User searchUserByUserName(String username) {
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("username",username);
                return userDao.selectOne(wrapper);
        }


        /**
         * 通过用户ID查询用户的具体信息
         * @param id
         * @return
         */
        @Override
        public User queryUserByUserId(int id) {
                return userDao.selectById(id);
        }


}
