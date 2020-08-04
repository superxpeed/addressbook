package com.addressbook.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable

@Entity("locks")
class Lock(id: String, login: String) : Serializable {

    @Id
    @QuerySqlField(index = true)
    var id: String? = id

    @QuerySqlField(index = true)
    var login: String? = login
}