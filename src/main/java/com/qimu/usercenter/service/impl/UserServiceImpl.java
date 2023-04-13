package com.qimu.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qimu.usercenter.common.ErrorCode;
import com.qimu.usercenter.exception.BusinessException;
import com.qimu.usercenter.mapper.UserMapper;
import com.qimu.usercenter.modle.domain.User;
import com.qimu.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

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
    public long userRegistration(String username, String userAccount, String userPassword, String checkPassword) {
        // 1. 非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入参数不能为空 (>_<) !");
        }
        // 2. 账户长度不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短,账号不能小于于4位 (>_<) !");
        }
        // 2. 账户长度不大于16位
        if (userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过长,账号不能大于16位 (>_<) !");
        }
        // 3. 密码就不小于8位吧
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短,不能小于8位 (>_<) !");
        }
        //  5. 账户不包含特殊字符
        // 匹配由数字、小写字母、大写字母组成的字符串,且字符串的长度至少为1个字符
        String pattern = "[0-9a-zA-Z]+";
        if (!userAccount.matches(pattern)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符 (>_<) !");
        }
        // 6. 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入密码不一致,请重新输入 (>_<) !");
        }
        // 4. 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在,请重新输入 (>_<) !");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        if (StringUtils.isAnyBlank(username)) {
            user.setUsername("游客 (" + userAccount + ")");
        } else {
            user.setUsername(username);
        }
        user.setAvatarUrl("https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/typora/yk.png");
        boolean saveResult = this.save(user);
        log.info("user=" + user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败,请重新输入 (>_<) !");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入参数不能为空 (>_<) !");
        }
        // 2. 账户长度不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短,账号不能小于于4位 (>_<) !");
        }
        // 2. 账户长度不大于16位
        if (userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过长,账号不能大于16位 (>_<) !");
        }
        // 3. 密码就不小于8位吧
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短,不能小于8位 (>_<) !");
        }
        //  5. 账户不包含特殊字符
        String pattern = "[0-9a-zA-Z]+";
        if (!userAccount.matches(pattern)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符 (>_<) !");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(userQueryWrapper);

        // 用户不存在
        if (user == null) {
            log.info("user login failed,userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误 (>_<) !");
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
        safeUser.setCreateTime(originUser.getCreateTime());
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




