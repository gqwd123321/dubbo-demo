package com.example.dubboconsumer;

import com.gaoqi.api.DemoService;
import com.gaoqi.api.TestService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@EnableDubbo
@SpringBootTest
class DubboConsumerApplicationTests {

    @DubboReference
    DemoService demoService;

    @DubboReference
    TestService testService;

    @Test
    void  contextLoads() {
        String test = testService.testTask("hello");
        String hello = demoService.sayHello("world");
        System.out.println(test);
        System.out.println("--------------");
        System.out.println(hello);
    }

}
