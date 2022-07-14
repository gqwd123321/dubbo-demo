package com.gaoqi.dubboprovider.service.impl;

import com.gaoqi.api.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

@DubboService
@Component
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("from client"+name);
        return "hello "+name;
    }
}
