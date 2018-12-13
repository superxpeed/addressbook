package com.webapp.dto;

import com.webapp.model.MenuEntry;

import java.io.Serializable;

public class MenuEntryDto implements Serializable {

    private String id;

    private String parentId;

    private String url;

    private String name;

    public MenuEntryDto(MenuEntry menuEntry) {
        this.id = menuEntry.getId();
        this.parentId = menuEntry.getParentId();
        this.url = menuEntry.getUrl();
        this.name = menuEntry.getName();
    }

    public MenuEntryDto(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public MenuEntryDto() { }

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
