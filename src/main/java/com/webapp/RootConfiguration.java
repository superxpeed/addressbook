package com.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@ComponentScan("com.webapp")
public class RootConfiguration {

    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    GridStarter gridStarter(){
        return new GridStarter();
    }

    @Bean
    GridStopper gridStopper(){
        return new GridStopper();
    }
}
