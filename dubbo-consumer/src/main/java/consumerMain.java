import com.gaoqi.action.AnnotationAction;
import com.gaoqi.config.ConsumerConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

}
