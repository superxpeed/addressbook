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

    private var userName: String? = null
    var authorities: Collection<GrantedAuthority>? = null

    @Autowired
    var igniteDao: IgniteClient? = null

    @PostConstruct
    fun init() {
        userName = SecurityContextHolder.getContext().authentication.name
        authorities = ArrayList(SecurityContextHolder.getContext().authentication.authorities)
    }

    fun getCurrentUser(): String? {
        return userName
    }

    @PreDestroy
    fun destroy() {
        userName?.let { igniteDao?.unlockAllRecordsForUser(it) }
    }
}
