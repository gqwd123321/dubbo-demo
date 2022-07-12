package com.gaoqi.provider.impl;

import com.gaoqi.api.DemoService;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        System.out.println("from client ：" + name);
        return "hello, " + name;
    }
}
