package com.qimu.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimu.usercenter.modle.domain.User;
import com.qimu.usercenter.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;


@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUsers() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    @Test
    public void setInsert() {
        User user = new User();
        user.setUsername("请问");
        user.setUserAccount("12qqq");
        user.setAvatarUrl("wwqqq");
        user.setUserPassword("111qqww");
        user.setPhone("1221134141");
        user.setEmail("122wqwq");
        boolean save = userService.save(user);
//        Assert.a(save);
        Assertions.assertEquals(save, false);
    }

    @Test
    void userRegistration() {
        String userAccount = "";
        String userPassword = "123456";
        String checkPassword = "123456";
        // 1. 非空
        long l = userService.userRegistration(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(l, -1);
        // 2. 账户长度不小于4位
        userAccount = "qm";
        l = userService.userRegistration(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(l, -1);
        // 3. 密码就不小于8位吧
        userPassword = "123456";
        checkPassword = "123456";
        l = userService.userRegistration(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(l, -1);
        // 5. 账户不包含特殊字符
        userAccount = "@##AA";
        l = userService.userRegistration(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(l, -1);
        // 6. 密码和校验密码相同
        userPassword = "12345678";
        checkPassword = "12345679";
        l = userService.userRegistration(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(l, -1);
        // 4. 账户不能重复

        userAccount = "qieruww";
        userPassword = "qwer1234";
        checkPassword = "qwer1234";
        l = userService.userRegistration(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(l > 0);

    }

    @Test
    void testGetUser() {
        String userAccount = "qweer55";
        String userPassword = "12345689";
        // String checkPassword = "123456789";
        // long l = userService.userRegistration(userAccount, userPassword, checkPassword);

        final String SALT = "qimu";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount)
                .eq("userPassword", encryptPassword);
        User user = userService.getOne(queryWrapper);
        if (!ObjectUtils.allNotNull(user)) {
            System.out.println("登陆失败");
        } else {
            System.out.println("登陆成功");
        }

        // User b = userService.userLogin(userAccount, userPassword);
        // Assertions.assertTrue(b);
    }

    @Test
    void testP() {
        String userAccount = "";
        String pattern = "[0-9a-zA-Z]+";
        if (!userAccount.matches(pattern)) {
            System.out.println("包含");
        }
    }
}