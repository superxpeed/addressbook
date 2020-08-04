package com.addressbook.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.util.*
import javax.persistence.ElementCollection

@Entity("menus")
@javax.persistence.Entity
class MenuEntry {

    @Id
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
