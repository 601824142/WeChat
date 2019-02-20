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
public class Friends implements Serializable {

    private int userId;//用户ID
    private int friendId;//好友ID
    private Date ctime;//创建时间
    private int status;//好友状态 0-普通好友、1-黑名单、2-已删除

    @TableField(exist = false)
    private User friend;

}
