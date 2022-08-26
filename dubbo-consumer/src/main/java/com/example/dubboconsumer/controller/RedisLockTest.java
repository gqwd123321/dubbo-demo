package com.example.dubboconsumer.controller;


import com.gaoqi.api.TestService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisLockTest {
    @DubboReference
    private TestService testService;

    @RequestMapping("/dlTest")
    public String dlTest(){
        return testService.testTask();
    }
}
