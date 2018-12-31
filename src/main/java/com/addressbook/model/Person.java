package com.addressbook.model;

import com.addressbook.dto.PersonDto;
import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
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
    private Double salary;

    public Person(PersonDto personDto) {
        id = personDto.getId();
    }

    public Person(Organization org, String firstName, String lastName, Double salary, String resume) {
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
