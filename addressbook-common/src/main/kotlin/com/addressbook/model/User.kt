package com.addressbook.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable

@Entity("users")
class User(@Id @QuerySqlField(index = true) var login: String?, @QuerySqlField(index = true) var password: String?, var roles: List<String>?) : Serializable {

    constructor() : this(null, null, null)

}
