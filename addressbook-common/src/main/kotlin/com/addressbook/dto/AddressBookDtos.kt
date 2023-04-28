package com.addressbook.dto

import com.addressbook.model.Contact
import com.addressbook.model.Document
import com.addressbook.model.Organization
import com.addressbook.model.Person
import java.io.Serializable
import java.text.SimpleDateFormat

val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

class ContactDto constructor(contact: Contact? = null) : Serializable {
    var personId: String? = null
    var id: String? = null
    var type: String? = ""
    var data: String? = null
    var description: String? = null

    init {
        this.personId = contact?.personId
        this.id = contact?.contactId
        this.type = "" + contact?.type?.ordinal
        this.data = contact?.data
        this.description = contact?.description
    }
}

class DocumentDto(id: String?,
                  personId: String?,
                  name: String?,
                  url: String?,
                  checksum: String?,
                  size: String?,
                  createDate: String?) : Serializable {

    var id: String? = null
    var personId: String? = null
    var name: String? = null
    var url: String? = null
    var checksum: String? = null
    var size: String? = null
    var createDate: String? = null

    constructor(document: Document?) : this(document?.id,
            document?.personId,
            document?.name,
            null,
            document?.checksum,
            document?.size,
            dateFormatter.format(document?.createDate))

    init {
        this.id = id
        this.name = name
        this.personId = personId
        this.url = url
        this.checksum = checksum
        this.size = size
        this.createDate = createDate
    }
}

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
            "" + organization?.type?.ordinal,
            dateFormatter.format(organization?.lastUpdated))

    init {
        this.id = id
        this.name = name
        this.street = street
        this.zip = zip
        this.type = type
        this.lastUpdated = lastUpdated
    }
}

class PersonDto constructor(person: Person? = null) : Serializable {

    var id: String? = null
    var orgId: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var resume: String? = null
    var salary: String? = null

    init {
        this.id = person?.id
        this.orgId = person?.orgId
        this.firstName = person?.firstName
        this.lastName = person?.lastName
        this.resume = person?.resume
        this.salary = person?.salary
    }
}