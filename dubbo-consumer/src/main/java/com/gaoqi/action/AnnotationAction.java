package com.gaoqi.action;

import com.gaoqi.api.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component("annotationAction")
public class AnnotationAction {
    @DubboReference
    DemoService demoService;

    public String doSayHello(String name){
        return demoService.sayHello(name);
    }
}
