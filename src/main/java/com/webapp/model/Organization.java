package com.webapp.model;

import com.webapp.dto.OrganizationDto;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.sql.Timestamp;
import java.util.UUID;

public class Organization {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField(index = true)
    private String name;

    @QuerySqlField(index = true)
    private Address addr;

    @QuerySqlField(index = true)
    private OrganizationType type;

    @QuerySqlField(index = true)
    private Timestamp lastUpdated;

    public Organization(OrganizationDto organizationDto) {
        id = organizationDto.getId();
    }

    public Organization() {}

    public Organization(String name) {
        id = UUID.randomUUID().toString();

        this.name = name;
    }

    public Organization(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Organization(String name, Address addr, OrganizationType type, Timestamp lastUpdated) {
        id = UUID.randomUUID().toString();

        this.name = name;
        this.addr = addr;
        this.type = type;

        this.lastUpdated = lastUpdated;
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

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override public String toString() {
        return "Organization [id=" + id +
                ", name=" + name +
                ", address=" + addr +
                ", type=" + type +
                ", lastUpdated=" + lastUpdated + ']';
    }
}
