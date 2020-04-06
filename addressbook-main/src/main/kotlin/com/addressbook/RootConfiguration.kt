package com.addressbook;

import com.addressbook.ignite.GridStarter;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.http.HttpSessionListener;

@Configuration
open class RootConfiguration {

    @Bean
    open fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter();
    }

    @Bean
    open fun gridStarter(): GridStarter {
        return GridStarter();
    }

    @Bean
    open fun sessionListener(): ServletListenerRegistrationBean<HttpSessionListener> {
        return ServletListenerRegistrationBean<HttpSessionListener>(SessionListener());
    }
}
