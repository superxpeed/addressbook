package com.addressbook.ignite

import com.addressbook.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class UserCreator {

    @Autowired
    var igniteDao: IgniteClient? = null

    @PostConstruct
    fun initUsers() {
        val encoder = BCryptPasswordEncoder()
        val adminRoles = ArrayList<String>()
        adminRoles.add("USER")
        adminRoles.add("ADMIN")
        val admin = User("admin", encoder.encode("adminPass"), adminRoles)
        igniteDao?.createOrUpdateUser(admin)
        val user = User("user", encoder.encode("userPass"), Collections.singletonList("USER"))
        igniteDao?.createOrUpdateUser(user)
    }
}