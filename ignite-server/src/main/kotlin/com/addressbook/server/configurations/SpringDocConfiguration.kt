package com.addressbook.server.configurations

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDocConfiguration {
    @Bean
    fun springAddressBookOpenAPI(): OpenAPI? {
        return OpenAPI()
                .info(Info().title("Apache Ignite Data Persistence Layer REST API")
                        .description("Spring Ecosystem Backbone Single Page Application")
                        .version("v1.0")
                        .license(License().name("Apache 2.0").url("https://springdoc.org")))
                .externalDocs(ExternalDocumentation()
                        .description("Addressbook")
                        .url("https://github.com/dredwardhyde/addressbook"))
    }
}
