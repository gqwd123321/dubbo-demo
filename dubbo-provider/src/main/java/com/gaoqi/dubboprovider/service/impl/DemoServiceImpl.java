package com.gaoqi.dubboprovider.service.impl;

import com.gaoqi.api.DemoService;
import com.gaoqi.dubboprovider.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DubboService

public class DemoServiceImpl implements DemoService {


    @Override
    public String sayHello(String name) {
        System.out.println("from client"+name);
        return "hello "+name;
    }

}
