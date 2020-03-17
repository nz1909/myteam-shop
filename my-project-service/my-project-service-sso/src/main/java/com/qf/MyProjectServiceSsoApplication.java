package com.qf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.qf.mapper")
public class MyProjectServiceSsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProjectServiceSsoApplication.class, args);
    }

}
