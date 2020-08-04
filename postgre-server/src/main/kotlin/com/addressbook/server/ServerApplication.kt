package com.addressbook.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@EntityScan(basePackages = ["com.addressbook.model"])
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class, ManagementWebSecurityAutoConfiguration::class])
class ServerApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ServerApplication::class.java, *args)
        }
    }
}