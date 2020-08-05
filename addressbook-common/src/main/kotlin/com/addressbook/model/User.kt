package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable
import javax.persistence.ElementCollection

@dev.morphia.annotations.Entity("users")
@javax.persistence.Entity
@javax.persistence.Table(name = "addressbook_users")
class User(@javax.persistence.Id
           @dev.morphia.annotations.Id
           @QuerySqlField(index = true) var login: String?,
           @QuerySqlField(index = true) var password: String?,
           @ElementCollection var roles: List<String>?) : Serializable {

    constructor() : this(null, null, null)
}
