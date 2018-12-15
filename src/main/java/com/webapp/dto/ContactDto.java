package com.webapp.dto;

import com.webapp.model.Contact;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ContactDto implements Serializable {

    private String personId;
    private String id;
    private String type;
    private String data;
    private String description;

    public ContactDto(Contact contact) {
        this.personId = contact.getPersonId();
        this.id = contact.getContactId();
        this.type = contact.getType().ordinal() + "";
        this.data = contact.getData();
        this.description = contact.getDescription();
    }
}
