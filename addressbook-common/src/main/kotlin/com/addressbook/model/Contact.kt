package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.util.*

@javax.persistence.Entity
@javax.persistence.Table(name = "contacts")
@dev.morphia.annotations.Entity("contacts")
class Contact(@QuerySqlField(index = true) var personId: String?,
              @QuerySqlField(index = true) var type: ContactType?,
              @QuerySqlField(index = true) var data: String?,
              @QuerySqlField(index = true) var description: String?) {

    constructor() : this(null, null, null, null)

    @dev.morphia.annotations.Id
    @javax.persistence.Id
    @QuerySqlField(index = true)
    var contactId: String? = null

    init {
        this.contactId = UUID.randomUUID().toString()
    }
}

enum class ContactType {
    MOBILE_PHONE,
    HOME_PHONE,
    ADDRESS,
    EMAIL
}