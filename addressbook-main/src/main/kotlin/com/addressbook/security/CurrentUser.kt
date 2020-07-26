package com.addressbook.security

import com.addressbook.ignite.IgniteClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import java.io.Serializable
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
class CurrentUser : Serializable {

    lateinit var userName: String
    lateinit var authorities: Collection<GrantedAuthority>

    @Autowired
    lateinit var igniteDao: IgniteClient

    @PostConstruct
    fun init() {
        userName = SecurityContextHolder.getContext().authentication.name
        authorities = ArrayList(SecurityContextHolder.getContext().authentication.authorities)
    }

    @PreDestroy
    fun destroy() {
        userName.let { igniteDao.unlockAllRecordsForUser(it) }
    }
}
