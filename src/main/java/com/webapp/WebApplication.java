package com.webapp;

@SuppressWarnings("unused")
@SpringBootApplication
@ComponentScan(basePackages="com.twitter")
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
