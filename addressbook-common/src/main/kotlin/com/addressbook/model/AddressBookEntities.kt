package com.addressbook.model

import com.addressbook.dto.OrganizationDto
import dev.morphia.annotations.Embedded
import org.apache.ignite.binary.BinaryReader
import org.apache.ignite.binary.BinaryWriter
import org.apache.ignite.binary.Binarylizable
import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.Embeddable

@javax.persistence.Entity
@javax.persistence.Table(name = "organizations")
@dev.morphia.annotations.Entity("organizations")
class Organization constructor(id: String?, name: String?, addr: Address?, type: OrganizationType?, lastUpdated: Timestamp?) {

    @dev.morphia.annotations.Id
    @javax.persistence.Id
    @QuerySqlField(index = true)
    var id: String? = null

    @QuerySqlField(index = true)
    var name: String? = null

    @QuerySqlField(index = true)
    @Embedded
    @javax.persistence.Embedded
    var addr: Address? = null

    @QuerySqlField(index = true)
    var type: OrganizationType? = null

    @QuerySqlField(index = true)
    var lastUpdated: Timestamp? = null

    constructor(organizationDto: OrganizationDto) : this(organizationDto.id, null, null, null, null)
    constructor(name: String) : this(UUID.randomUUID().toString(), name, null, null, null)
    constructor() : this(UUID.randomUUID().toString(), null, null, null, null)
    constructor(id: String, name: String) : this(id, name, null, null, null)
    constructor(name: String, addr: Address, type: OrganizationType, lastUpdated: Timestamp) : this(UUID.randomUUID().toString(), name, addr, type, lastUpdated)

    init {
        this.id = id
        this.name = name
        this.addr = addr
        this.type = type
        this.lastUpdated = lastUpdated
    }
}

enum class OrganizationType(s: String) {

    NON_PROFIT("Non profit"),
    PRIVATE("Private"),
    GOVERNMENT("Government"),
    PUBLIC("Public");

    var eng: String? = null

    init {
        this.eng = s
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = "persons")
@dev.morphia.annotations.Entity("persons")
class Person : Serializable {

    @dev.morphia.annotations.Id
    @javax.persistence.Id
    @QuerySqlField(index = true)
    var id: String? = null

    @QuerySqlField(index = true)
    var orgId: String? = null

    @QuerySqlField(index = true)
    var firstName: String? = null

    @QuerySqlField(index = true)
    var lastName: String? = null

    @QuerySqlField(index = true)
    var resume: String? = null

    @QuerySqlField(index = true)
    var salary: String? = null

    constructor(org: Organization, firstName: String, lastName: String, salary: String, resume: String) : this() {
        this.orgId = org.id
        this.firstName = firstName
        this.lastName = lastName
        this.salary = salary
        this.resume = resume
    }

    constructor() {
        id = UUID.randomUUID().toString()
    }

    constructor(id: String, firstName: String, lastName: String) : this() {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
    }
}

@Embeddable
class Address(street: String?, zip: String?) : Binarylizable {

    @QuerySqlField(index = true)
    var street: String? = null

    @QuerySqlField(index = true)
    var zip: String? = null

    constructor() : this(null, null)

    init {
        this.street = street
        this.zip = zip
    }

    override fun writeBinary(writer: BinaryWriter) {
        writer.writeString("street", street)
        writer.writeString("zip", zip)
    }

    override fun readBinary(reader: BinaryReader) {
        street = reader.readString("street")
        zip = reader.readString("zip")
    }
}

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