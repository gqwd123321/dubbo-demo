package com.gaoqi.dubboprovider.service.impl;

import com.gaoqi.api.UserService;
import com.gaoqi.dubboprovider.mapper.UserMapper;
import com.gaoqi.dubboprovider.models.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@DubboService
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

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
    public String queryUserByUsername(String username) {
        //先尝试从redis中获取对象
        User user = (User) redisTemplate.opsForValue().get(username);
        //如果没有获取到就去数据库获取
        if(null==user){
            System.out.println("缓存中没有该用户");
            user=userMapper.queryUserByUsername(username);
            //如果数据库中存在该记录，则写入redis，返回该条记录
            if(null!=user){
                redisTemplate.opsForValue().set(username,user);
                String userJson = JSON.toJSONString(user);
                return userJson;
            }
        }
        return JSON.toJSONString(user);
    }
}
