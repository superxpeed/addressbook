package com.addressbook.dto

class BreadcrumbDto constructor(name: String?, url: String?) {
    var name: String? = null
    var url: String? = null

    init {
        this.name = name
        this.url = url
    }
}