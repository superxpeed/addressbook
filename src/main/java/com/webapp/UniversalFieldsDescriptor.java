package com.webapp;

import com.webapp.dto.OrganizationDto;
import com.webapp.dto.PersonDto;
import com.webapp.model.FieldDescription;
import com.webapp.model.Organization;
import com.webapp.model.Person;

import java.util.LinkedHashMap;
import java.util.Map;

public class UniversalFieldsDescriptor {

    public static final String ORGANIZATION_CACHE = "com.webapp.model.Organization";
    public static final String PERSON_CACHE = "com.webapp.model.Person";

    private static Map<String, FieldDescription> fieldDescriptionMapOrganization = new LinkedHashMap<>();
    private static Map<String, FieldDescription> fieldDescriptionMapPerson = new LinkedHashMap<>();
    private static Map<String, Map<String, FieldDescription>> fieldDescriptionMaps = new LinkedHashMap<>();
    private static Map<String, Class> cacheClasses = new LinkedHashMap<>();
    private static Map<String, Class> dtoClasses = new LinkedHashMap<>();

    static {
        fieldDescriptionMapOrganization.put("id", new FieldDescription("id", "ID", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapOrganization.put("name", new FieldDescription("name", "Name", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapOrganization.put("street", new FieldDescription("street", "Street", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapOrganization.put("zip", new FieldDescription("zip", "Zip", "java.lang.Long", "170px",  false, false));
        fieldDescriptionMapOrganization.put("type", new FieldDescription("type", "Type", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapOrganization.put("lastUpdated", new FieldDescription("lastUpdated", "Last updated", "java.util.Date", "170px",  false, false));

        fieldDescriptionMapPerson.put("id", new FieldDescription("id", "ID", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapPerson.put("orgId", new FieldDescription("orgId", "Organization", "java.lang.String", "170px",  true, false));
        fieldDescriptionMapPerson.put("firstName", new FieldDescription("firstName", "First name", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapPerson.put("lastName", new FieldDescription("lastName", "Last name", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapPerson.put("resume", new FieldDescription("resume", "Resume", "java.lang.String", "170px",  false, false));
        fieldDescriptionMapPerson.put("salary", new FieldDescription("salary", "Salary", "java.lang.Long", "170px",  false, false));

        fieldDescriptionMaps.put(ORGANIZATION_CACHE, fieldDescriptionMapOrganization);
        fieldDescriptionMaps.put(PERSON_CACHE, fieldDescriptionMapPerson);

        cacheClasses.put(ORGANIZATION_CACHE, Organization.class);
        cacheClasses.put(PERSON_CACHE, Person.class);

        dtoClasses.put(ORGANIZATION_CACHE, OrganizationDto.class);
        dtoClasses.put(PERSON_CACHE, PersonDto.class);

    }
    public static Map<String, FieldDescription> getFieldDescriptionMap(String cache){
        return fieldDescriptionMaps.get(cache);
    }
    public static Class getCacheClass(String cache){
        return cacheClasses.get(cache);
    }
    public static Map<String, Class> getCacheClasses(){
        return cacheClasses;
    }
    public static Class getDtoClass(String cache){
        return dtoClasses.get(cache);
    }
    public static Map<String, Class> getDtoClasses(){
        return dtoClasses;
    }

}
