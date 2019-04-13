package com.addressbook.security;

import com.addressbook.ignite.GridDAO;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUser {

    private String userName;
    private Collection<? extends GrantedAuthority> authorities;

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public String getCurrentUser() {
        return userName;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @PreDestroy
    private void destroy() {
        GridDAO.unlockAllRecordsForUser(userName);
    }
}
