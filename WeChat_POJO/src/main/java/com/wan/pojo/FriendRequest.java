package com.wan.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 万星明
 * @Date 2019/2/18
 */
@Data
public class FriendRequest implements Serializable {

    private  int id;
    private  int requestId;//申请人
    private  int responseId;//被申请人
    private Date stime;//申请时间
    private String content;//申请内容
    private int status;//申请状态

    @TableField(exist = false)
    private  User requestUser;


}
