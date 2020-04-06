package com.addressbook;

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = ["com.addressbook"])
open class AddressBookApplication {
    companion object {
        @JvmStatic
        fun main(args : Array<String>) {
            SpringApplication.run(AddressBookApplication::class.java, *args);
        }
    }
}