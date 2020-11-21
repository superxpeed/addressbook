package com.addressbook.configurations

import com.addressbook.listeners.SessionListener
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import javax.servlet.http.HttpSessionListener

@Configuration
class RootConfiguration {

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter()
    }

    @Bean
    fun sessionListener(): ServletListenerRegistrationBean<HttpSessionListener> {
        return ServletListenerRegistrationBean(SessionListener())
    }
}
