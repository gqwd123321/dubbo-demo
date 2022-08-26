package com.gaoqi.dubboprovider.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gaoqi.api.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisLockTestController {
    @Autowired
    private TestService testService;



    @SentinelResource("dlTest")
    @RequestMapping("/dlTest")
    public String dlTest(){
        return testService.testTask();
    }
}
