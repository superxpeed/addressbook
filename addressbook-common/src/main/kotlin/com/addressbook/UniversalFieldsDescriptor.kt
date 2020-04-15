package com.addressbook

import com.addressbook.dto.ContactDto
import com.addressbook.dto.FieldDescription
import com.addressbook.dto.OrganizationDto
import com.addressbook.dto.PersonDto
import com.addressbook.model.*
import java.util.*

object UniversalFieldsDescriptor {

    const val ORGANIZATION_CACHE = "com.addressbook.model.Organization"
    const val PERSON_CACHE = "com.addressbook.model.Person"
    const val CONTACT_CACHE = "com.addressbook.model.Contact"
    const val MENU_CACHE = "com.addressbook.model.MenuEntry"
    const val USER_CACHE = "com.addressbook.model.User"
    const val LOCK_RECORD_CACHE = "java.lang.String"

    private const val STRING_MODEL_TYPE = "java.lang.String"
    private const val DATE_MODEL_TYPE = "java.util.Date"
    private const val STANDARD_COLUMN_WIDTH = "170px"

    val fieldDescriptionMapOrganization = LinkedHashMap<String, FieldDescription>()
    val fieldDescriptionMapPerson = LinkedHashMap<String, FieldDescription>()
    private val fieldDescriptionMaps = LinkedHashMap<String, Map<String, FieldDescription>>()
    private val cacheClasses = LinkedHashMap<String, Class<*>>()
    private val dtoClasses = LinkedHashMap<String, Class<*>>()

    init {
        fieldDescriptionMapOrganization["id"] = FieldDescription("id", "ID", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["name"] = FieldDescription("name", "Name", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["street"] = FieldDescription("street", "Street", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["zip"] = FieldDescription("zip", "Zip", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["type"] = FieldDescription("type", "Type", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["lastUpdated"] = FieldDescription("lastUpdated", "Last updated", STANDARD_COLUMN_WIDTH, DATE_MODEL_TYPE)

        fieldDescriptionMapPerson["id"] = FieldDescription("id", "ID", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["orgId"] = FieldDescription("orgId", "Organization", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["firstName"] = FieldDescription("firstName", "First name", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["lastName"] = FieldDescription("lastName", "Last name", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["resume"] = FieldDescription("resume", "Resume", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["salary"] = FieldDescription("salary", "Salary", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)

        fieldDescriptionMaps[ORGANIZATION_CACHE] = fieldDescriptionMapOrganization
        fieldDescriptionMaps[PERSON_CACHE] = fieldDescriptionMapPerson

        cacheClasses[ORGANIZATION_CACHE] = Organization::class.java
        cacheClasses[PERSON_CACHE] = Person::class.java
        cacheClasses[CONTACT_CACHE] = Contact::class.java
        cacheClasses[MENU_CACHE] = MenuEntry::class.java
        cacheClasses[USER_CACHE] = User::class.java
        cacheClasses[LOCK_RECORD_CACHE] = String::class.java

        dtoClasses[ORGANIZATION_CACHE] = OrganizationDto::class.java
        dtoClasses[PERSON_CACHE] = PersonDto::class.java
        dtoClasses[CONTACT_CACHE] = ContactDto::class.java

    }

    fun getFieldDescriptionMap(cache: String): Map<String, FieldDescription>? {
        return fieldDescriptionMaps[cache]
    }

    fun getCacheClass(cache: String): Class<*>? {
        return cacheClasses[cache]
    }

    fun getCacheClasses(): Map<String, Class<*>> {
        return cacheClasses
    }

    fun getDtoClass(cache: String): Class<*>? {
        return dtoClasses[cache]
    }
}
