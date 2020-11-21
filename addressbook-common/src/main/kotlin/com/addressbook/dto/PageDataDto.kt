package com.addressbook.dto

class PageDataDto<T>(data: T?, fieldDescriptionDtoMap: Map<String, FieldDescriptionDto>?) {

    var data: T? = null
    var fieldDescriptionMap: Map<String, FieldDescriptionDto>? = null

    constructor(data: T?) : this(data, null)

    init {
        this.data = data
        this.fieldDescriptionMap = fieldDescriptionDtoMap
    }
}
