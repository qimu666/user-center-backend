package com.qimu.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qimu.usercenter.modle.domain.User;
import com.qimu.usercenter.service.UserService;
import com.qimu.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author qimu
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-01-13 23:17:21
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




