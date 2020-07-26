package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.util.*

class Contact(@QuerySqlField(index = true) var personId: String?, @QuerySqlField(index = true) var type: ContactType?, @QuerySqlField(index = true) var data: String?, @QuerySqlField(index = true) var description: String?) {
    constructor() : this(null, null, null, null)

    @QuerySqlField(index = true)
    var contactId: String? = null

    init {
        this.contactId = UUID.randomUUID().toString()
    }
}
