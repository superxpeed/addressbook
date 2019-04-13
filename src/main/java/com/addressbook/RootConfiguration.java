package com.addressbook;

import com.addressbook.ignite.GridStarter;
import com.addressbook.ignite.GridStopper;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.http.HttpSessionListener;

@Configuration
public class RootConfiguration {

    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    GridStarter gridStarter() {
        return new GridStarter();
    }

    @Bean
    GridStopper gridStopper() {
        return new GridStopper();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(new SessionListener());
    }
}
