package com.gaoqi.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo(scanBasePackages = "com.gaoqi.action")
@PropertySource("classpath:/dubbo-consumer.properties")
@ComponentScan(value = {"com.gaoqi.action"})
public class ConsumerConfiguration {

}
