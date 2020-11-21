package com.addressbook

import com.addressbook.dto.ContactDto
import com.addressbook.dto.FieldDescriptionDto
import com.addressbook.dto.OrganizationDto
import com.addressbook.dto.PersonDto
import com.addressbook.model.*
import java.util.*

object FieldDescriptor {

    const val ORGANIZATION_CACHE = "com.addressbook.model.Organization"
    const val PERSON_CACHE = "com.addressbook.model.Person"
    const val CONTACT_CACHE = "com.addressbook.model.Contact"
    const val MENU_CACHE = "com.addressbook.model.MenuEntry"
    const val USER_CACHE = "com.addressbook.model.User"
    const val LOCK_RECORD_CACHE = "java.lang.String"

    private const val STRING_MODEL_TYPE = "java.lang.String"
    private const val DATE_MODEL_TYPE = "java.util.Date"
    private const val STANDARD_COLUMN_WIDTH = "170px"

    val fieldDescriptionMapOrganization = LinkedHashMap<String, FieldDescriptionDto>()
    val fieldDescriptionMapPerson = LinkedHashMap<String, FieldDescriptionDto>()
    private val fieldDescriptionMaps = LinkedHashMap<String, Map<String, FieldDescriptionDto>>()
    private val cacheClasses = LinkedHashMap<String, Class<*>>()
    private val dtoClasses = LinkedHashMap<String, Class<*>>()

    init {
        fieldDescriptionMapOrganization["id"] = FieldDescriptionDto("id", "ID", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["name"] = FieldDescriptionDto("name", "Name", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["street"] = FieldDescriptionDto("street", "Street", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["zip"] = FieldDescriptionDto("zip", "Zip", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["type"] = FieldDescriptionDto("type", "Type", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapOrganization["lastUpdated"] = FieldDescriptionDto("lastUpdated", "Last updated", STANDARD_COLUMN_WIDTH, DATE_MODEL_TYPE)

        fieldDescriptionMapPerson["id"] = FieldDescriptionDto("id", "ID", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["orgId"] = FieldDescriptionDto("orgId", "Organization", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["firstName"] = FieldDescriptionDto("firstName", "First name", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["lastName"] = FieldDescriptionDto("lastName", "Last name", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["resume"] = FieldDescriptionDto("resume", "Resume", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)
        fieldDescriptionMapPerson["salary"] = FieldDescriptionDto("salary", "Salary", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE)

        fieldDescriptionMaps[ORGANIZATION_CACHE] = fieldDescriptionMapOrganization
        fieldDescriptionMaps[PERSON_CACHE] = fieldDescriptionMapPerson

        cacheClasses[ORGANIZATION_CACHE] = Organization::class.java
        cacheClasses[PERSON_CACHE] = Person::class.java
        cacheClasses[CONTACT_CACHE] = Contact::class.java
        cacheClasses[MENU_CACHE] = MenuEntry::class.java
        cacheClasses[USER_CACHE] = User::class.java
        cacheClasses[LOCK_RECORD_CACHE] = Lock::class.java

        dtoClasses[ORGANIZATION_CACHE] = OrganizationDto::class.java
        dtoClasses[PERSON_CACHE] = PersonDto::class.java
        dtoClasses[CONTACT_CACHE] = ContactDto::class.java

    }

    fun getFieldDescriptionMap(cache: String): Map<String, FieldDescriptionDto>? {
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
