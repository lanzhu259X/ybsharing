package com.yiban.sharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan({"com.yiban.sharing"})
public class App {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class);
    }
}
