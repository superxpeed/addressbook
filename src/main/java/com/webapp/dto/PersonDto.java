package com.webapp.dto;

import com.webapp.model.Person;

import java.io.Serializable;

public class PersonDto implements Serializable {
    private String id;
    private String orgId;
    private String firstName;
    private String lastName;
    private String resume;
    private String salary;

    public PersonDto() {}

    public PersonDto(Person person){
        this.id = person.getId();
        this.orgId = person.getOrgId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.resume = person.getResume();
        this.salary = String.valueOf(person.getSalary());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "id='" + id + '\'' +
                ", orgId='" + orgId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", resume='" + resume + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
