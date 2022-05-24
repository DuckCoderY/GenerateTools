package com.rich.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * @Author: l_y
 * @Date: 2022/4/22 13:40
 **/
@SpringBootApplication
@ComponentScan(basePackages = "com.rich")
public class ManyGenerateApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManyGenerateApplication.class, args);
    }
}
