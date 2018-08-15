package com.webapp.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.UUID;

public class Contact {

    @QuerySqlField(index = true)
    private String personId;

    @QuerySqlField(index = true)
    private String contactId;

    @QuerySqlField(index = true)
    private ContactType type;

    @QuerySqlField(index = true)
    private String data;

    @QuerySqlField(index = true)
    private String description;

    public Contact() {
        this.contactId = UUID.randomUUID().toString();
    }

    public Contact(String personId, ContactType type, String data, String description) {
        this.personId = personId;
        this.contactId = UUID.randomUUID().toString();
        this.type = type;
        this.data = data;
        this.description = description;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "personId='" + personId + '\'' +
                ", contactId='" + contactId + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
