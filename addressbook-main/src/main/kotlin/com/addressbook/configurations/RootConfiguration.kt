package com.addressbook.configurations

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class RootConfiguration {

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter()
    }

    @Bean
    fun springShopOpenAPI(): OpenAPI? {
        return OpenAPI()
                .info(Info().title("Addressbook REST API")
                        .description("Spring Ecosystem Backbone Single Page Application")
                        .version("v1.0")
                        .license(License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(ExternalDocumentation()
                        .description("Addressbook")
                        .url("https://github.com/dredwardhyde/addressbook"))
    }
}
