package com.addressbook.dto

class TableDataDto<T> constructor(data: List<T>?, totalDataSize: Int?) {

    var data: List<T>? = null
    var totalDataSize: Int? = null

    init {
        this.data = data
        this.totalDataSize = totalDataSize
    }

    constructor(data: List<T>?) : this(data, data?.size)
}
