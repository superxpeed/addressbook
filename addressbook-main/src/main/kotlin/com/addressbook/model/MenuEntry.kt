package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.util.UUID

class MenuEntry {

    @QuerySqlField(index = true)
    var id: String? = null

    @QuerySqlField(index = true)
    var parentId: String? = null

    @QuerySqlField(index = true)
    var url: String? = null

    @QuerySqlField(index = true)
    var name: String? = null

    var roles: List<String>? = null

    init {
        this.id = UUID.randomUUID().toString()
    }
}
