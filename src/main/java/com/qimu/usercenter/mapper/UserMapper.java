package com.qimu.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimu.usercenter.modle.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qimu
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2023-01-13 23:17:21
 * @Entity generator.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




