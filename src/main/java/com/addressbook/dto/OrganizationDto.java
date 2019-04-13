package com.addressbook.dto;

import com.addressbook.model.Organization;
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
public class OrganizationDto implements Serializable {

    private String id;
    private String name;
    private String street;
    private String zip;
    private String type;
    private String lastUpdated;

    public OrganizationDto(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.street = organization.getAddr().getStreet();
        this.zip = organization.getAddr().getZip() + "";
        this.type = organization.getType().getEng();
        this.lastUpdated = organization.getLastUpdated().toString();
    }
}
