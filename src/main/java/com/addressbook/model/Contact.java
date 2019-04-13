package com.addressbook.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.UUID;

@Getter
@Setter
@ToString
@SuppressWarnings("unused")
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
}
