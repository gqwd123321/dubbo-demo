package com.example.dubboconsumer;

import com.gaoqi.api.TestService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@EnableDubbo
@SpringBootApplication
public class DubboConsumerApplication {


    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class, args);
    }

}
