package com.pavel.jogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JoggerBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(JoggerBackendApplication.class, args);
    }
}

