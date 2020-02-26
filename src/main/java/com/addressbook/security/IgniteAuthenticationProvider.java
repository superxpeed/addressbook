package com.addressbook.security;

import com.addressbook.ignite.GridDAO;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IgniteAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(final Authentication authentication) {
        // Here I receive login and password (plaintext) from UI
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        // Then I read user using that login as a key
        com.addressbook.model.User user = GridDAO.getUserByLogin(login);
        if (user == null) throw new BadCredentialsException("Incorrect login");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // And since password stored in Ignite is encoded, I use encoder to check if it's right
        if (!encoder.matches(password, user.getPassword())) throw new BadCredentialsException("Incorrect password");
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        // Important: add ROLE_ because it's default Spring prefix for roles
        user.getRoles().forEach(x -> grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + x)));
        UserDetails principal = new User(login, password, grantedAuths);
        // And return plain simple JSESSIONID token
        return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
