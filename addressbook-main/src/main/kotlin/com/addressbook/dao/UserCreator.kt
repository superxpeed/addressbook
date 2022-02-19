package com.addressbook.dao

import com.addressbook.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class UserCreator {

    @Autowired
    lateinit var dao: DaoClient

    @PostConstruct
    fun initUsers() {
        val encoder = BCryptPasswordEncoder()
        dao.createOrUpdateUser(User("admin", encoder.encode("adminPass"), listOf("USER", "ADMIN")))
        dao.createOrUpdateUser(User("user", encoder.encode("userPass"), Collections.singletonList("USER")))
    }
}