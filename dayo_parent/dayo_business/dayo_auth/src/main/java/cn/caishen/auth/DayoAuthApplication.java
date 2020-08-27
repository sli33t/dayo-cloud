package cn.caishen.auth;

import cn.caishen.auth.config.JwtProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LB
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(JwtProperties.class)
@EnableDubbo(scanBasePackages = "cn.caishen.auth.service")
@ComponentScan(basePackages = {"cn.caishen"})
public class DayoAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayoAuthApplication.class, args);
    }
}
