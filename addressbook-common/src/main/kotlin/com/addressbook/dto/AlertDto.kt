package com.addressbook.dto

import java.io.Serializable

class AlertDto(headline: String?, type: String?, message: String?) : Serializable {
    companion object {
        const val SUCCESS: String = "success"
        const val WARNING: String = "warning"
        const val DANGER: String = "danger"
        const val RECORD_PREFIX: String = "Record id "
    }

    var headline: String? = null
    var type: String? = null
    var message: String? = null

    init {
        this.headline = headline
        this.message = message
        this.type = type
    }

    constructor() : this(null, null, null)
}
