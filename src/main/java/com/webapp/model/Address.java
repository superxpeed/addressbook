package com.webapp.model;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Address implements Binarylizable {

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @QuerySqlField(index = true)
    private String street;

    @QuerySqlField(index = true)
    private int zip;

    public Address() {
        // No-op.
    }

    public Address(String street, int zip) {
        this.street = street;
        this.zip = zip;
    }

    @Override public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
        writer.writeString("street", street);
        writer.writeInt("zip", zip);
    }

    @Override public void readBinary(BinaryReader reader) throws BinaryObjectException {
        street = reader.readString("street");
        zip = reader.readInt("zip");
    }

    @Override public String toString() {
        return "Address [street=" + street +
                ", zip=" + zip + ']';
    }
}
