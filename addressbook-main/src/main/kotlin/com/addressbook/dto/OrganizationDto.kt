package com.addressbook.dto

import com.addressbook.model.Organization
import java.io.Serializable

class OrganizationDto(id: String?,
                      name: String?,
                      street: String?,
                      zip: String?,
                      type: String?,
                      lastUpdated: String?) : Serializable {

    var id: String? = null
    var name: String? = null
    var street: String? = null
    var zip: String? = null
    var type: String? = null
    var lastUpdated: String? = null

    constructor(organization: Organization?) : this(organization?.id,
            organization?.name,
            organization?.addr?.street,
            organization?.addr?.zip,
            organization?.type?.eng,
            organization?.lastUpdated.toString())

    init {
        this.id = id
        this.name = name
        this.street = street
        this.zip = zip
        this.type = type
        this.lastUpdated = lastUpdated
    }
}
