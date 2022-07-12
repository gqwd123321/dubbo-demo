import com.gaoqi.action.AnnotationAction;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.Scanner;


public class consumerMain {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        System.out.println("dubbo服务消费端已启动...");
        AnnotationAction annotationAction = (AnnotationAction)context.getBean("annotationAction");
        String hello = annotationAction.doSayHello("world");
        System.out.println(hello);
        new Scanner(System.in).next();
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "com.gaoqi.action")
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan(value = {"com.gaoqi.action"})
    static public class ConsumerConfiguration {

    }
}
