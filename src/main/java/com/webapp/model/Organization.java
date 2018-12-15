package com.webapp.model;

import com.webapp.dto.OrganizationDto;
import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
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


    public Organization(String name) {
        this.id = UUID.randomUUID().toString();
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
}
