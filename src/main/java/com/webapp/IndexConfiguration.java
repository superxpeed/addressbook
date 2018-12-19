package com.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IndexConfiguration {

    @Bean
    IndexWebController indexWebController(){
        return new IndexWebController();
    }
}
