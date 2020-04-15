package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable

class User(login: String, password: String, roles: List<String>) : Serializable {

    @QuerySqlField(index = true)
    var login: String? = login

    @QuerySqlField(index = true)
    var password: String? = password

    var roles: List<String>? = roles

}
