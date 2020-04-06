package com.addressbook.model;

enum class OrganizationType(s: String) {

    NON_PROFIT("Non profit"),
    PRIVATE("Private"),
    GOVERNMENT("Government"),
    PUBLIC("Public");

    var eng: String? = null
}
