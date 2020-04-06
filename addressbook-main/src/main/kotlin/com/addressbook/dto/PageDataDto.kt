package com.addressbook.dto

import com.addressbook.model.FieldDescription

class PageDataDto<T> {

    var data: T? = null
    var fieldDescriptionMap: Map<String, FieldDescription>? = null
}
