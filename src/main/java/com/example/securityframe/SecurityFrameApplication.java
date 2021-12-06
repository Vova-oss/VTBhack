package com.example.securityframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecurityFrameApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityFrameApplication.class, args);
    }

}
