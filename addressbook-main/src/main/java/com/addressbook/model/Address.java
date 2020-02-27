package com.addressbook.model;

import lombok.*;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address implements Binarylizable {

    @QuerySqlField(index = true)
    private String street;

    @QuerySqlField(index = true)
    private String zip;

    @Override
    public void writeBinary(BinaryWriter writer) {
        writer.writeString("street", street);
        writer.writeString("zip", zip);
    }

    @Override
    public void readBinary(BinaryReader reader) {
        street = reader.readString("street");
        zip = reader.readString("zip");
    }
}
