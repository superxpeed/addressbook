package com.addressbook.dto

import java.io.Serializable

class FilterDto : Serializable {

    var name: String? = null
    var value: String? = null
    var comparator: String? = null
    var type: String? = null

}
