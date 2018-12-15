package com.webapp.model;

import lombok.*;
import org.apache.ignite.binary.BinaryObjectException;
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
    private int zip;

    @Override public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
        writer.writeString("street", street);
        writer.writeInt("zip", zip);
    }

    @Override public void readBinary(BinaryReader reader) throws BinaryObjectException {
        street = reader.readString("street");
        zip = reader.readInt("zip");
    }
}
