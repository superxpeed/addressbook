package com.addressbook.dto

class PageDataDto<T>(data: T?, fieldDescriptionMap: Map<String, FieldDescription>?) {

    var data: T? = null
    var fieldDescriptionMap: Map<String, FieldDescription>? = null

    constructor(data: T?) : this(data, null)

    init {
        this.data = data
        this.fieldDescriptionMap = fieldDescriptionMap
    }
}
