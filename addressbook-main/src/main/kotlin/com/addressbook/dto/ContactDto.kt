package com.addressbook.dto

import com.addressbook.model.Contact
import java.io.Serializable

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
