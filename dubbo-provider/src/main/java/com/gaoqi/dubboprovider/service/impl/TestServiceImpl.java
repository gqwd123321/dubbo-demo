package com.gaoqi.dubboprovider.service.impl;

import com.gaoqi.api.TestService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

@DubboService
@Component
public class TestServiceImpl implements TestService {

    @Override
    public String testTask(String name) {
        System.out.println("test start");
        return "test "+name;
    }
}
