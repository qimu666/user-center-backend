package com.qimu.usercenter.modle.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别  (0 - 男 , 1 - 女)
     */
    private Integer gender;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户账号状态( 0 - 正常 )
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0 - 删除 , 1 - 正常)
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色( 0 - 默认用户 ， 1 - 管理员)
     */
    private Integer userRole;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}