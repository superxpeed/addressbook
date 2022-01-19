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
