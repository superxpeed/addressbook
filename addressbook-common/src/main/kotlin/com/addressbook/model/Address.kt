package com.addressbook.model

import org.apache.ignite.binary.BinaryReader
import org.apache.ignite.binary.BinaryWriter
import org.apache.ignite.binary.Binarylizable
import org.apache.ignite.cache.query.annotations.QuerySqlField
import javax.persistence.Embeddable

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
