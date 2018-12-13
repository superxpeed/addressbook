package com.webapp.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.UUID;

public class MenuEntry {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField(index = true)
    private String parentId;

    @QuerySqlField(index = true)
    private String url;

    @QuerySqlField(index = true)
    private String name;

    public MenuEntry(String parentId, String url, String name) {
        this.id = UUID.randomUUID().toString();
        this.parentId = parentId;
        this.url = url;
        this.name = name;
    }

    public MenuEntry() {
        this.id = UUID.randomUUID().toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
