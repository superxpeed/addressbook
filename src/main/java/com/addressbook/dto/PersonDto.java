package com.addressbook.dto;

import com.addressbook.model.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuppressWarnings("unused")
public class PersonDto implements Serializable {

    private String id;
    private String orgId;
    private String firstName;
    private String lastName;
    private String resume;
    private String salary;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.orgId = person.getOrgId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.resume = person.getResume();
        this.salary = String.valueOf(person.getSalary());
    }
}
