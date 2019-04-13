package com.addressbook.ignite;

import com.addressbook.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class UserCreator {
    public static void initUsers() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<String> adminRoles = new ArrayList<>();
        adminRoles.add("USER");
        adminRoles.add("ADMIN");
        User admin = new User("admin", encoder.encode("adminPass"), adminRoles);
        GridDAO.createOrUpdateUser(admin);
        User user = new User("user", encoder.encode("userPass"), Collections.singletonList("USER"));
        GridDAO.createOrUpdateUser(user);
    }
}