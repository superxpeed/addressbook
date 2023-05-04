package com.addressbook.dto

import com.addressbook.model.Contact
import com.addressbook.model.Document
import com.addressbook.model.Organization
import com.addressbook.model.Person
import java.io.Serializable
import java.text.SimpleDateFormat

val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

class ContactDto(id: String?,
                 personId: String?,
                 type: String?,
                 data: String?,
                 description: String?) : Serializable {

    var id: String? = null
    var personId: String? = null
    var type: String? = ""
    var data: String? = null
    var description: String? = null

    constructor(contact: Contact?) : this(contact?.contactId,
            contact?.personId,
            contact?.type?.ordinal?.toString() ?: "",
            contact?.data,
            contact?.description)

    init {
        this.id = id
        this.personId = personId
        this.type = type
        this.data = data
        this.description = description
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
            organization?.type?.ordinal?.toString() ?: "",
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

class PersonDto(id: String?,
                orgId: String?,
                firstName: String?,
                lastName: String?,
                resume: String?,
                salary: String?) : Serializable {

    var id: String? = null
    var orgId: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var resume: String? = null
    var salary: String? = null

    constructor(person: Person?) : this(person?.id,
            person?.orgId,
            person?.firstName,
            person?.lastName,
            person?.resume,
            person?.salary)

    init {
        this.id = id
        this.orgId = orgId
        this.firstName = firstName
        this.lastName = lastName
        this.resume = resume
        this.salary = salary
    }
}