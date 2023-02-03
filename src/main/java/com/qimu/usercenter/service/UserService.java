package com.qimu.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qimu.usercenter.modle.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qimu
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-01-13 23:17:21
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 新注册用户的id
     */
    long userRegistration(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      记录用户的登录态
     * @return 登陆成功的用户信息（脱敏之后）
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser 用户信息
     * @return 脱敏后的用户信息
     */
    User getSateUser(User originUser);

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    Integer loginOut(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);
}
