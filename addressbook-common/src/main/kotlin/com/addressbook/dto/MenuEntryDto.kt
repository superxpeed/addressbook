package com.addressbook.dto

import com.addressbook.model.MenuEntry
import java.io.Serializable

class MenuEntryDto(menuEntry: MenuEntry? = null) : Serializable {

    var id: String? = null
    var parentId: String? = null
    var url: String? = null
    var name: String? = null
    var roles: List<String>? = null

    init {
        this.id = menuEntry?.id
        this.parentId = menuEntry?.parentId
        this.url = menuEntry?.url
        this.name = menuEntry?.name
        this.roles = menuEntry?.roles
    }

    constructor(url: String, name: String, roles: List<String>) : this() {
        this.url = url
        this.name = name
        this.roles = roles
    }
}
