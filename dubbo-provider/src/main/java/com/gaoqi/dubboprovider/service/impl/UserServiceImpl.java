package com.gaoqi.dubboprovider.service.impl;

import com.gaoqi.api.UserService;
import com.gaoqi.dubboprovider.mapper.UserMapper;
import com.gaoqi.dubboprovider.models.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;

@DubboService
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public String queryUserById(Integer userId) {
        User user = userMapper.queryUserById(userId);
        if(user!=null){
            String userJson = JSON.toJSONString(user);
            return userJson;
        }
        System.out.println("该用户不存在");
        return "该用户不存在";

    }
}
