package com.addressbook.model;


import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    @QuerySqlField(index = true)
    private String login;

    @QuerySqlField(index = true)
    private String password;

    private List<String> roles;
}
