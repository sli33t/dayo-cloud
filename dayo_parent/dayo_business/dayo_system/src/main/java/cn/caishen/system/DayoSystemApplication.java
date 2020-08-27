package cn.caishen.system;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LB
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "cn.caishen.system.service")
@ComponentScan(basePackages = {"cn.caishen"})
public class DayoSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayoSystemApplication.class, args);
    }
}
