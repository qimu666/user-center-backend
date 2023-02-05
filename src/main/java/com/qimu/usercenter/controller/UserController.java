package com.qimu.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimu.usercenter.common.BaseResponse;
import com.qimu.usercenter.common.ErrorCode;
import com.qimu.usercenter.common.ResultUtil;
import com.qimu.usercenter.exception.BusinessException;
import com.qimu.usercenter.modle.domain.User;
import com.qimu.usercenter.modle.request.UserLoginRequest;
import com.qimu.usercenter.modle.request.UserRegisterRequest;
import com.qimu.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse<User> login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtil.success(user, "登陆成功");
    }

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegistration(userAccount, userPassword, checkPassword);
        return ResultUtil.success(result,"注册成功");
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object objUser = request.getSession().getAttribute(LOGIN_USER_STATUS);
        User currentUser = (User) objUser;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        return ResultUtil.success(userService.getSateUser(user));
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchList(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        List<User> users = userService.list(userQueryWrapper);
        List<User> result = users.stream().map(user -> userService.getSateUser(user)).collect(Collectors.toList());
        return ResultUtil.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(Long id, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtil.success(userService.removeById(id));
    }

    @PostMapping("/loginOut")
    public BaseResponse<Integer> loginOut(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtil.success(userService.loginOut(request));
    }
}
