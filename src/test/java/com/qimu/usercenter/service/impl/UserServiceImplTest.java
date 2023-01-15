package com.qimu.usercenter.service.impl;

import com.qimu.usercenter.modle.domain.User;
import com.qimu.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Assertions.assertEquals(save,false);
    }

}