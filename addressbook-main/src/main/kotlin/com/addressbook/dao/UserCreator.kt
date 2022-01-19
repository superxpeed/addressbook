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
        val adminRoles = ArrayList<String>()
        adminRoles.add("USER")
        adminRoles.add("ADMIN")
        val admin = User("admin", encoder.encode("adminPass"), adminRoles)
        dao.createOrUpdateUser(admin)
        val user = User("user", encoder.encode("userPass"), Collections.singletonList("USER"))
        dao.createOrUpdateUser(user)
    }
}