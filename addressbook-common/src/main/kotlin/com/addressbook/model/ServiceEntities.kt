package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable
import java.util.*
import javax.persistence.ElementCollection

@javax.persistence.Entity
@javax.persistence.Table(name = "addressbook_users")
@dev.morphia.annotations.Entity("addressbook_users")
class User(@javax.persistence.Id
           @dev.morphia.annotations.Id
           @QuerySqlField(index = true) var login: String,
           @QuerySqlField(index = true) var password: String,
           @ElementCollection var roles: List<String>) : Serializable {

    constructor() : this("", "", Collections.emptyList<String>())
}

@javax.persistence.Entity
@javax.persistence.Table(name = "locks")
@dev.morphia.annotations.Entity("locks")
class Lock(@javax.persistence.Id
           @dev.morphia.annotations.Id
           @QuerySqlField(index = true) var id: String?,
           @QuerySqlField(index = true) var login: String?) : Serializable {

    constructor() : this(null, null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Lock

        if (id != other.id) return false
        if (login != other.login) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (login?.hashCode() ?: 0)
        return result
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = "menus")
@dev.morphia.annotations.Entity("menus")
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