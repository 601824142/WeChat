package com.wan.controller;

import com.wan.pojo.User;
import com.wan.service.IUserService;
import com.wan.util.Constact;
import com.wan.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 万星明
 * @Date 2019/2/15
 */
@RestController //直接返回json数据
@RequestMapping("/user")
@Slf4j//lombok提供的日志注解
public class UserController {

    @Value("${fdfs.serverip}")
    private String fdfsUrl;

    @Autowired
    private IUserService userService;


    /**
     * 用户注册方法
     * @return
     */
    @RequestMapping("/register")
    public ResultData register(User user){
        log.debug("进入注册！！");

        //调用服务注册用户
        int register = userService.register(user);
        //新建返回结果数据对象,由于不用返回具体的泛型数据,则泛型不写
        ResultData resultData = new ResultData();

        //根据服务返回结果,设置返回前端数据
        if (register >0){//注册成功
            resultData.setCode(Constact.SUCCESS_CODE);
            resultData.setMessage("注册成功！");
        }else if(register == -1){//注册失败,用户名已存在
            resultData.setCode(Constact.USERNAME_ARLEADE_HAVE_CODE);
            resultData.setMessage("注册失败,用户名已存在！");
        }else {//其他未知错误
            resultData.setCode(Constact.ERROR_CODE);
            resultData.setMessage("未知错误,即将反馈！");
        }

        //返回给前端结果数据
        return resultData;
    }



    /**
     * 用户登陆方法
     * @return
     */
    @RequestMapping("/login")
    public ResultData<User> login(String username,String password,String uuid){
        //调用服务查询用户
        User user = userService.login(username, password, uuid);

        if(user != null){  //如果返回不为空
            System.out.println("1");
            //用户头像地址设置
            user.setHeader(fdfsUrl+user.getHeader());
            System.out.println("2");
            user.setHeaderCrm(fdfsUrl+user.getHeaderCrm());

            System.out.println("3");
            //设置二维码
            user.setQrCode(fdfsUrl+user.getQrCode());

            return  ResultData.createResultData(user);
        }else {               //如果返回为空
            return ResultData.createResultData(Constact.USERNAME_PASSWORD_ERROR_CODE,"用户名或密码错误！");
        }

    }

    /**
     * 修改用户头像功能
     * @param header
     * @param headerCrm
     * @param userId
     * @return
     */
    @RequestMapping("/updateheader")
    public ResultData<Boolean>  updateUserHeader(String header,String headerCrm,Integer userId){
        System.out.println("进入修改头像到数据库:"+userId);
        //调用方法,修改数据库头像路径
        int result = userService.updateUserHeader(header, headerCrm, userId);
        //如果返回的结果大于0
        if(result>0){
            return ResultData.createResultData(true);
        }
        return ResultData.createResultData(Constact.ERROR_CODE,"头像修改失败！");
    }


    /**
     * 根据用户名查询用户信息(搜索添加好友服务)
     * @param username
     * @return
     */
    @RequestMapping("/searchByUserName")
    public ResultData<User> searchUserByUserName(String username){
        System.out.println("通过姓名查询："+username);
        //调用服务根据用户名查询用户
        User user = userService.searchUserByUserName(username);
        if(user !=null){
            System.out.println("判断是否为空！");
            //拼接头像以及头像缩略图
            user.setHeader(fdfsUrl+user.getHeader());
            user.setHeaderCrm(fdfsUrl+user.getHeaderCrm());
        }

        //返回包装类
        return ResultData.createResultData(user);
    }


    @RequestMapping("/queryUserById")
    public User queryUserById(int id){
        //调用服务,根据用户ID查询用户信息
        User user = userService.queryUserByUserId(id);
        if (user !=null){
            //用户头像地址设置
            user.setHeader(fdfsUrl+user.getHeader());
            user.setHeaderCrm(fdfsUrl+user.getHeaderCrm());
        }

        //设置二维码
        user.setQrCode(fdfsUrl+user.getQrCode());
        return user;
    }



}
