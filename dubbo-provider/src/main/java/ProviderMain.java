import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

public class ProviderMain {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ProviderConfiguration.class);
        context.start();
        System.out.println("注册成功，如想退出，按任意键退出");
        System.in.read(); // 按任意键退出
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "com.gaoqi.provider.impl")
    @PropertySource("classpath:/dubbo-provider.properties")
    static public class ProviderConfiguration {

    }

}
