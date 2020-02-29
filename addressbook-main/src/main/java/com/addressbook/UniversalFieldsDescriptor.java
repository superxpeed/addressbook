package com.addressbook;

import com.addressbook.dto.ContactDto;
import com.addressbook.dto.OrganizationDto;
import com.addressbook.dto.PersonDto;
import com.addressbook.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniversalFieldsDescriptor {

    public static final String ORGANIZATION_CACHE = "com.addressbook.model.Organization";
    public static final String PERSON_CACHE = "com.addressbook.model.Person";
    public static final String CONTACT_CACHE = "com.addressbook.model.Contact";
    public static final String MENU_CACHE = "com.addressbook.model.MenuEntry";
    public static final String USER_CACHE = "com.addressbook.model.User";
    public static final String LOCK_RECORD_CACHE = "java.lang.String";

    public static final String STRING_MODEL_TYPE = "java.lang.String";
    public static final String DATE_MODEL_TYPE = "java.util.Date";
    public static final String STANDARD_COLUMN_WIDTH = "170px";

    private static Map<String, FieldDescription> fieldDescriptionMapOrganization = new LinkedHashMap<>();
    private static Map<String, FieldDescription> fieldDescriptionMapPerson = new LinkedHashMap<>();
    private static Map<String, Map<String, FieldDescription>> fieldDescriptionMaps = new LinkedHashMap<>();
    private static Map<String, Class<?>> cacheClasses = new LinkedHashMap<>();
    private static Map<String, Class<?>> dtoClasses = new LinkedHashMap<>();

    static {
        fieldDescriptionMapOrganization.put("id",           new FieldDescription("id",          "ID",           STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapOrganization.put("name",         new FieldDescription("name",        "Name",         STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapOrganization.put("street",       new FieldDescription("street",      "Street",       STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapOrganization.put("zip",          new FieldDescription("zip",         "Zip",          STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapOrganization.put("type",         new FieldDescription("type",        "Type",         STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapOrganization.put("lastUpdated",  new FieldDescription("lastUpdated", "Last updated", STANDARD_COLUMN_WIDTH,DATE_MODEL_TYPE));

        fieldDescriptionMapPerson.put("id",         new FieldDescription("id",          "ID",           STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapPerson.put("orgId",      new FieldDescription("orgId",       "Organization", STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapPerson.put("firstName",  new FieldDescription("firstName",   "First name",   STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapPerson.put("lastName",   new FieldDescription("lastName",    "Last name",    STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapPerson.put("resume",     new FieldDescription("resume",      "Resume",       STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));
        fieldDescriptionMapPerson.put("salary",     new FieldDescription("salary",      "Salary",       STANDARD_COLUMN_WIDTH, STRING_MODEL_TYPE));

        fieldDescriptionMaps.put(ORGANIZATION_CACHE, fieldDescriptionMapOrganization);
        fieldDescriptionMaps.put(PERSON_CACHE, fieldDescriptionMapPerson);

        cacheClasses.put(ORGANIZATION_CACHE, Organization.class);
        cacheClasses.put(PERSON_CACHE, Person.class);
        cacheClasses.put(CONTACT_CACHE, Contact.class);
        cacheClasses.put(MENU_CACHE, MenuEntry.class);
        cacheClasses.put(USER_CACHE, User.class);
        cacheClasses.put(LOCK_RECORD_CACHE, String.class);

        dtoClasses.put(ORGANIZATION_CACHE, OrganizationDto.class);
        dtoClasses.put(PERSON_CACHE, PersonDto.class);
        dtoClasses.put(CONTACT_CACHE, ContactDto.class);

    }

    public static Map<String, FieldDescription> getFieldDescriptionMap(String cache) {
        return fieldDescriptionMaps.get(cache);
    }

    public static Class<?> getCacheClass(String cache) {
        return cacheClasses.get(cache);
    }

    public static Map<String, Class<?>> getCacheClasses() {
        return cacheClasses;
    }

    public static Class<?> getDtoClass(String cache) {
        return dtoClasses.get(cache);
    }
}
