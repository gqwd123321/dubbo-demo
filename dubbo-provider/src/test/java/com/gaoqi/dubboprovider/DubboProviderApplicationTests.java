package com.gaoqi.dubboprovider;

import com.gaoqi.dubboprovider.RedisStudy.DelayQueueProducter;
import com.gaoqi.dubboprovider.RedisStudy.RedisDelayQueue;
import com.gaoqi.dubboprovider.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootTest
class DubboProviderApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    @Autowired
    DelayQueueProducter delayQueueProducter;
    @Test
    void contextLoads() {
//        redisTemplate.opsForValue().set(user.getUsername(),user);

//        User user = userMapper.queryUserByUsername("wangyue");
//        redisTemplate.opsForValue().set(user.getUsername(),user);
//        System.out.println(redisTemplate.opsForValue().get(user.getUsername()));

    }

}
