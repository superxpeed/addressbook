package com.addressbook.security

import com.addressbook.ignite.IgniteDAOClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class IgniteAuthenticationProvider : AuthenticationProvider {

    @Autowired
    var igniteDao: IgniteDAOClient? = null;

    override fun authenticate(authentication: Authentication): Authentication {
        // Here I receive login and password (plaintext) from UI
        val login = authentication.name
        val password = authentication.credentials.toString()
        // Then I read user using that login as a key
        val user: com.addressbook.model.User = igniteDao?.getUserByLogin(login)
                ?: throw BadCredentialsException("Incorrect login")
        val encoder = BCryptPasswordEncoder()
        // And since password stored in Ignite is encoded, I use encoder to check if it's right
        if (!encoder.matches(password, user.password)) throw BadCredentialsException("Incorrect password")
        val grantedAuths = ArrayList<GrantedAuthority>()
        // Important: add ROLE_ because it's default Spring prefix for roles
        user.roles?.forEach { x -> grantedAuths.add(SimpleGrantedAuthority("ROLE_$x")) }
        val principal = User(login, password, grantedAuths)
        // And return plain simple JSESSIONID token
        return UsernamePasswordAuthenticationToken(principal, password, grantedAuths)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
