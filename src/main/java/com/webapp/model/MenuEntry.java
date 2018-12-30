package com.webapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class MenuEntry {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField(index = true)
    private String parentId;

    @QuerySqlField(index = true)
    private String url;

    @QuerySqlField(index = true)
    private String name;

    private List<String> roles;
    
    public MenuEntry() {
        this.id = UUID.randomUUID().toString();
    }
}
