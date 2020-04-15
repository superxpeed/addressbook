package com.addressbook.model

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable
import java.util.*

class Person : Serializable {

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
