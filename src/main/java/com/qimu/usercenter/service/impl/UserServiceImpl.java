package com.qimu.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qimu.usercenter.mapper.UserMapper;
import com.qimu.usercenter.modle.domain.User;
import com.qimu.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.qimu.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.qimu.usercenter.contant.UserConstant.LOGIN_USER_STATUS;

/**
 * @author qimu
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-01-13 23:17:21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 盐值 (混淆密码)
     */
    private static final String SALT = "qimu";

    /**
     * 用户账号注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 成功的条数
     */
    @Override
    public long userRegistration(String userAccount, String userPassword, String checkPassword) {
        // 1. 非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        // 2. 账户长度不小于4位
        if (userAccount.length() < 4) {
            return -1;
        }
        // 3. 密码就不小于8位吧
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        //  5. 账户不包含特殊字符
        String pattern = "[0-9a-zA-Z]+";
        if (!userAccount.matches(pattern)) {
            return -1;
        }
        // 6. 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 4. 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        System.out.println(user.getId());
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // 2. 账户长度不小于4位
        if (userAccount.length() < 4) {
            return null;
        }
        // 3. 密码就不小于8位吧
        if (userPassword.length() < 8) {
            return null;
        }
        //  5. 账户不包含特殊字符
        String pattern = "[0-9a-zA-Z]+";
        if (!userAccount.matches(pattern)) {
            return null;
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(userQueryWrapper);

        // 用户不存在
        if (user == null) {
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }

        // 用户脱敏
        User safeUser = getSateUser(user);

        // 记录用户的登录态
        request.getSession().setAttribute(LOGIN_USER_STATUS, safeUser);
        return safeUser;
    }


    /**
     * 用户脱敏
     *
     * @param originUser 用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSateUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setCreateTime(new Date());
        return safeUser;
    }

    @Override
    public Integer loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute(LOGIN_USER_STATUS);
        return 1;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object objUser = request.getSession().getAttribute(LOGIN_USER_STATUS);
        User user = (User) objUser;
        // if (user == null || user.getUserRole() != ADMIN_ROLE) {
        //     return false;
        // }
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}




