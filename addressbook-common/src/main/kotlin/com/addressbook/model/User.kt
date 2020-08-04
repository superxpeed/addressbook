package com.addressbook.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable
import javax.persistence.ElementCollection

@Entity("users")
@javax.persistence.Entity
@javax.persistence.Table(name ="addressbook_users")
class User(@javax.persistence.Id
           @Id
           @QuerySqlField(index = true) var login: String?,
           @QuerySqlField(index = true) var password: String?,
           @ElementCollection var roles: List<String>?) : Serializable {

    constructor() : this(null, null, null)
}
