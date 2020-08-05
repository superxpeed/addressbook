package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable

@dev.morphia.annotations.Entity("locks")
@javax.persistence.Entity
class Lock(@javax.persistence.Id
           @dev.morphia.annotations.Id
           @QuerySqlField(index = true) var id: String?,
           @QuerySqlField(index = true) var login: String?) : Serializable {

    constructor() : this(null, null)
}