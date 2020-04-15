package com.addressbook.model

class FieldDescription(name: String, displayName: String, width: String, type: String) {

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
