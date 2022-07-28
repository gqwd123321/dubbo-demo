package com.example.dubboconsumer;

import com.gaoqi.api.DemoService;
import com.gaoqi.api.TestService;
import com.gaoqi.api.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@EnableDubbo
@SpringBootTest
class DubboConsumerApplicationTests {


    @DubboReference
    UserService userService;

    @Test
    void  contextLoads() {
        System.out.println("缓存中存在的测试结果："+userService.queryUserByUsername("wangyue"));
        System.out.println("缓存中不存在数据库中存在的测试结果："+userService.queryUserByUsername("gaoqi"));
        System.out.println("缓存中不存在，数据库中也不存在的测试结果："+userService.queryUserByUsername("gaogao"));
    }

}
