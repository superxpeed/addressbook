package com.addressbook.dto

import com.addressbook.model.Person
import java.io.Serializable

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
