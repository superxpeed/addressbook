package com.addressbook.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable

@Entity("locks")
@javax.persistence.Entity
class Lock(@javax.persistence.Id
           @Id
           @QuerySqlField(index = true) var id: String?,
           @QuerySqlField(index = true) var login: String?) : Serializable {

    constructor() : this(null, null)
}