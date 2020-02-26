package com.addressbook.model;

import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Person implements Serializable {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField(index = true)
    private String orgId;

    @QuerySqlField(index = true)
    private String firstName;

    @QuerySqlField(index = true)
    private String lastName;

    @QuerySqlField(index = true)
    private String resume;

    @QuerySqlField(index = true)
    private String salary;

    public Person() {
        id = UUID.randomUUID().toString();
    }

    public Person(Organization org, String firstName, String lastName, String salary, String resume) {
        this.id = UUID.randomUUID().toString();
        this.orgId = org.getId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.resume = resume;
    }

    public Person(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
