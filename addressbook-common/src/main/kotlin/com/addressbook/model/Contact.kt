package com.addressbook.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.util.*

@Entity("contacts")
@javax.persistence.Entity
class Contact(@QuerySqlField(index = true) var personId: String?,
              @QuerySqlField(index = true) var type: ContactType?,
              @QuerySqlField(index = true) var data: String?,
              @QuerySqlField(index = true) var description: String?) {

    constructor() : this(null, null, null, null)

    @Id
    @javax.persistence.Id
    @QuerySqlField(index = true)
    var contactId: String? = null

    init {
        this.contactId = UUID.randomUUID().toString()
    }
}
