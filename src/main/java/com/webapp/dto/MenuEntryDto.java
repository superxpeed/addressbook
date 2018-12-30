package com.webapp.dto;

import com.webapp.model.MenuEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MenuEntryDto implements Serializable {

    private String id;
    private String parentId;
    private String url;
    private String name;
    private List<String> roles;

    public MenuEntryDto(MenuEntry menuEntry) {
        this.id = menuEntry.getId();
        this.parentId = menuEntry.getParentId();
        this.url = menuEntry.getUrl();
        this.name = menuEntry.getName();
        this.roles = menuEntry.getRoles();
    }

    public MenuEntryDto(String url, String name, List<String> roles) {
        this.url = url;
        this.name = name;
        this.roles = roles;
    }
}
