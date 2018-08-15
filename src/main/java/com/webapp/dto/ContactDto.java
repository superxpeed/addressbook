package com.webapp.dto;

import com.webapp.model.Contact;

import java.io.Serializable;

public class ContactDto implements Serializable {

    private String personId;

    private String id;

    private String type;

    private String data;

    private String description;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContactDto(String personId, String id, String type, String data, String description) {
        this.personId = personId;
        this.id = id;
        this.type = type;
        this.data = data;
        this.description = description;
    }

    public ContactDto(Contact contact) {
        this.personId = contact.getPersonId();
        this.id = contact.getContactId();
        this.type = contact.getType().ordinal() + "";
        this.data = contact.getData();
        this.description = contact.getDescription();
    }

    public ContactDto() {
    }

    @Override
    public String toString() {
        return "ContactDto{" +
                "personId=" + personId +
                ", id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
