package com.addressbook.dto

import com.addressbook.model.MenuEntry
import java.io.Serializable

class AlertDto(headline: String?, type: String?, message: String?) : Serializable {
    companion object {
        const val SUCCESS: String = "success"
        const val WARNING: String = "warning"
        const val DANGER: String = "error"
        const val RECORD_PREFIX: String = "Record id "
    }

    var headline: String? = null
    var type: String? = null
    var message: String? = null

    init {
        this.headline = headline
        this.message = message
        this.type = type
    }

    constructor() : this(null, null, null)
}

class BreadcrumbDto constructor(name: String?, url: String?) {
    var name: String? = null
    var url: String? = null

    init {
        this.name = name
        this.url = url
    }
}

class FieldDescriptionDto(name: String, displayName: String, width: String, type: String) {

    var name: String? = null
    var displayName: String? = null
    var width: String? = null
    var type: String? = null

    init {
        this.name = name
        this.displayName = displayName
        this.width = width
        this.type = type
    }
}

class FilterDto : Serializable {

    var name: String? = null
    var value: String? = null
    var comparator: String? = null
    var type: String? = null

}

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

class BuildInfoDto : Serializable {

    var version: String? = null
    var artifact: String? = null
    var serverHost: String? = null
    var group: String? = null
    var name: String? = null
    var time: String? = null

}