package com.addressbook.model;

public enum OrganizationType {

    NON_PROFIT("Non profit"),
    PRIVATE("Private"),
    GOVERNMENT("Government"),
    PUBLIC("Public");

    private String eng;

    OrganizationType(String eng) { this.eng = eng; }

    public String getEng() { return eng; }
}
