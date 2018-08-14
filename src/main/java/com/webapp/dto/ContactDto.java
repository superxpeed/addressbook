package com.webapp.dto;

import java.io.Serializable;

public class ContactDto implements Serializable {

    private String personId;

    private String contactId;

    private int type;

    private String data;

    private String description;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public ContactDto(String personId, String contactId, int type, String data, String description) {
        this.personId = personId;
        this.contactId = contactId;
        this.type = type;
        this.data = data;
        this.description = description;
    }

    public ContactDto() {
    }

    @Override
    public String toString() {
        return "ContactDto{" +
                "personId=" + personId +
                ", contactId=" + contactId +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
