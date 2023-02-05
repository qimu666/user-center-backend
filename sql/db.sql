-- auto-generated definition
create database user_center;
-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '用户id'
        primary key,
    username     varchar(258)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '用户账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别 (0 - 男 1 - 女)',
    userPassword varchar(512)                       not null comment '用户密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户账号状态( 0 - 正常 )',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除(0 - 删除 , 1 - 正常)',
    userRole     int      default 0                 not null comment '用户角色( 0 - 默认用户 ， 1 - 管理员)'
)
    comment '用户表';

INSERT INTO `user` VALUES ('1', 'QiMU', 'qimu', 'https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/typora/logo.jpg', '0', 'ccb9d99c64e515ff8e62c8dbab9cb400', '17744608948', '2483482026@qq.com', '0', '2023-02-05 11:41:54', '2023-02-05 11:41:54', '0', '1');
INSERT INTO `user` VALUES ('2', 'yanglele', 'yanglele1234', 'https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/typora/yk.png', '1', '89375dcf9e7cc02574ebfb2303fd6cfe', null, null, '0', '2023-02-05 17:35:02', '2023-02-05 17:35:02', '0', '1');



