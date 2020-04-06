package com.addressbook.ignite

import com.addressbook.model.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.util.ArrayList
import java.util.Collections

class UserCreator {
    companion object {
        fun initUsers() {
            val encoder = BCryptPasswordEncoder()
            val adminRoles = ArrayList<String>()
            adminRoles.add("USER")
            adminRoles.add("ADMIN")
            val admin = User("admin", encoder.encode("adminPass"), adminRoles)
            GridDAO.createOrUpdateUser(admin)
            val user = User("user", encoder.encode("userPass"), Collections.singletonList("USER"))
            GridDAO.createOrUpdateUser(user)
        }
    }
}