package com.gaoqi.dubboprovider.service.impl;

import com.gaoqi.api.UserService;
import com.gaoqi.dubboprovider.mapper.UserMapper;
import com.gaoqi.dubboprovider.models.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;

import java.util.List;

@DubboService
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public String queryUserById(Integer userId) {
        User user = userMapper.queryUserById(userId);
        if(null!=user){
            String userJson = JSON.toJSONString(user);
            return userJson;
        }
        System.out.println("查询不到相关用户");
        return "该用户不存在";

    }

    @Override
    public String querryUserByUsername(String username) {
        List<User> users = userMapper.queryUserByUsername(username);
        if(0!=users.size()){
            String usersJson = JSON.toJSONString(users);
            return usersJson;
        }
        System.out.println("查询不到相关用户");
        return "该用户不存在";
    }
}
