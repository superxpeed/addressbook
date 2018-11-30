package com.webapp.dto;

import com.webapp.model.Organization;

import java.io.Serializable;

@SuppressWarnings("unused")
public class OrganizationDto implements Serializable {

    private String id;
    private String name;
    private String street;
    private String zip;
    private String type;
    private String lastUpdated;

    public OrganizationDto(){}

    public OrganizationDto(Organization organization){
        this.id = organization.getId();
        this.name = organization.getName();
        this.street = organization.getAddr().getStreet();
        this.zip = organization.getAddr().getZip()+"";
        this.type = organization.getType().getEng();
        this.lastUpdated = organization.getLastUpdated().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
