package com.example.dubboconsumer;

import com.gaoqi.api.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@EnableDubbo
@SpringBootTest
class DubboConsumerApplicationTests {

    @DubboReference
    DemoService demoService;

    @Test
    void  contextLoads() {
        String hello = demoService.sayHello("world");
        System.out.println(hello);
    }

}
