package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.util.*
import javax.persistence.ElementCollection

@dev.morphia.annotations.Entity("menus")
@javax.persistence.Entity
class MenuEntry {

    @dev.morphia.annotations.Id
    @javax.persistence.Id
    @QuerySqlField(index = true)
    var id: String? = null

    @QuerySqlField(index = true)
    var parentId: String? = null

    @QuerySqlField(index = true)
    var url: String? = null

    @QuerySqlField(index = true)
    var name: String? = null

    @ElementCollection
    var roles: List<String>? = null

    init {
        this.id = UUID.randomUUID().toString()
    }
}
