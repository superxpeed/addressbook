package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.util.UUID

class Contact(personId: String?, type: ContactType?, data: String?, description: String?) {
    constructor() : this(null, null, null, null)

    @QuerySqlField(index = true)
    var personId: String? = personId

    @QuerySqlField(index = true)
    var contactId: String? = null

    @QuerySqlField(index = true)
    var  type: ContactType? = type

    @QuerySqlField(index = true)
    var data: String? = data

    @QuerySqlField(index = true)
    var description: String? = description

    init {
        this.contactId = UUID.randomUUID().toString();
    }
}
