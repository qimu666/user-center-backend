package com.qimu.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimu.usercenter.modle.domain.User;
import com.qimu.usercenter.modle.request.UserLoginRequest;
import com.qimu.usercenter.modle.request.UserRegisterRequest;
import com.qimu.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.qimu.usercenter.contant.UserConstant.LOGIN_USER_STATUS;


/**
 * @author: QiMu
 * @Date: 2023年01月16日 20:23
 * @Version:1.0
 * @Description: 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest == null) {
            return null;
        }
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @PostMapping("/register")
    public Long register(@RequestBody UserRegisterRequest registerRequest) {
        if (registerRequest == null) {
            return null;
        }
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegistration(userAccount, userPassword, checkPassword);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        Object objUser = request.getSession().getAttribute(LOGIN_USER_STATUS);
        User currentUser = (User) objUser;
        if (currentUser == null) {
            return null;
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        return userService.getSateUser(user);
    }

    @GetMapping("/search")
    public List<User> searchList(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        List<User> users = userService.list(userQueryWrapper);
        return users.stream().map(user -> userService.getSateUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(Long id, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    @PostMapping("/loginOut")
    public Integer loginOut(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return userService.loginOut(request);
    }
}
