package cn.caishen.worklog;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LB
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "cn.caishen.worklog.service")
@ComponentScan(basePackages = {"cn.caishen"})
public class DayoWorklogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayoWorklogApplication.class, args);
    }
}
