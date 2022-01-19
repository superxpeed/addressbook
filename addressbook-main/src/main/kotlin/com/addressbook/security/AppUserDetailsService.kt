package com.addressbook.security

import com.addressbook.dao.DaoClient
import com.addressbook.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class AppUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var dao: DaoClient

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity: User = dao.getUserByLogin(username) ?: throw UsernameNotFoundException("User not found!")
        return AppUserDetails.fromUserEntityToCustomUserDetails(userEntity)
    }
}
