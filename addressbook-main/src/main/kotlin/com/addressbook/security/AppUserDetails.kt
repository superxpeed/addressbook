package com.addressbook.security

import com.addressbook.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class AppUserDetails : UserDetails {

    private lateinit var login: String

    private lateinit var password: String

    private var grantedAuthorities: Collection<GrantedAuthority> = emptyList()

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return grantedAuthorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return login
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        fun fromUserEntityToCustomUserDetails(userEntity: User): AppUserDetails {
            val appDetails = AppUserDetails()
            appDetails.login = userEntity.login
            appDetails.password = userEntity.password
            appDetails.grantedAuthorities = userEntity.roles.map { x -> SimpleGrantedAuthority("ROLE_$x") }.toList()
            return appDetails
        }
    }
}