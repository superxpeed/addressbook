package com.addressbook.server.dao

import com.addressbook.AddressBookDAO
import com.addressbook.UniversalFieldsDescriptor
import com.addressbook.dto.*
import com.addressbook.model.*
import com.mongodb.MongoClient
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.Sort
import org.springframework.stereotype.Controller
import java.lang.reflect.Constructor
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Controller
class DAO : AddressBookDAO {

    private val dateFormatEqual = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private val dateFormatOther = SimpleDateFormat("yyyy-MM-dd")

    private var mongoClient: MongoClient? = null
    private var dataStore: Datastore? = null

    @PostConstruct
    fun startClient() {
        if (Objects.nonNull(mongoClient)) return
        mongoClient = MongoClient(System.getenv("MONGO_HOST"), Integer.parseInt(System.getenv("MONGO_PORT")))
        val morphia = Morphia()
        morphia.mapPackage("com.addressbook.model")
        dataStore = morphia.createDatastore(mongoClient, "addressbook")
        dataStore?.ensureIndexes()
    }

    private fun <T : Any> getById(idColumn: String, id: String?, clazz: Class<T>): T? {
        val collection = dataStore?.createQuery(clazz)
                ?.field(idColumn)
                ?.equal(id)
                ?.find()
                ?.toList() as List<T>
        return if (collection.isNotEmpty()) collection[0] else null
    }

    override fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        var organization = getById("id", organizationDto.id, Organization::class.java)
        if (Objects.isNull(organization)) {
            organization = Organization(organizationDto)
        }
        organization?.type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
        organization?.lastUpdated = Timestamp(System.currentTimeMillis())
        organization?.name = organizationDto.name
        organization?.addr = Address(organizationDto.street, organizationDto.zip)
        dataStore?.save(organization)
        return organizationDto
    }

    override fun createOrUpdatePerson(personDto: PersonDto, user: String): PersonDto {
        var person = if (Objects.nonNull(personDto.id)) getById("id", personDto.id, Person::class.java) else null
        if (Objects.isNull(person)) {
            person = Person()
            personDto.id = person.id
        }
        person?.firstName = personDto.firstName
        person?.lastName = personDto.lastName
        person?.orgId = personDto.orgId
        person?.salary = personDto.salary
        person?.resume = personDto.resume
        dataStore?.save(person)
        return personDto
    }

    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, personId: String): List<ContactDto> {
        if (contactDtos.isEmpty()) return contactDtos
        var toDelete = getContactsByPersonId(personId).mapNotNull { it.id }
        val updated = contactDtos.mapNotNull { it.id }
        toDelete = toDelete.minus(updated)
        for (contactDto in contactDtos) {
            contactDto.personId = personId
            var contact = getById("contactId", contactDto.id, Contact::class.java)
            if (Objects.isNull(contact)) contact = Contact()
            contact?.data = contactDto.data
            contact?.description = contactDto.description
            contact?.personId = contactDto.personId
            contact?.type = ContactType.values()[Integer.parseInt(contactDto.type)]
            dataStore?.save(contact)
        }
        for (id in toDelete) {
            dataStore?.delete(getById("contactId", id, Contact::class.java))
        }
        return contactDtos
    }

    override fun createOrUpdateUser(newUser: User): String {
        var user = getById("login", newUser.login, User::class.java)
        if (Objects.nonNull(user)) {
            user?.password = newUser.password
            user?.roles = newUser.roles
        } else user = newUser
        dataStore?.save(user)
        return "OK"
    }

    override fun notLockedByUser(key: String, user: String): Boolean {
        val userLocked = getById("id", key, Lock::class.java)
        if (Objects.isNull(userLocked)) return false
        return user != userLocked?.login
    }

    override fun ifOrganizationExists(key: String): Boolean {
        return Objects.nonNull(getById("id", key, Organization::class.java))
    }

    override fun ifPersonExists(key: String): Boolean {
        return Objects.nonNull(getById("id", key, Person::class.java))
    }

    override fun ifContactExists(key: String): Boolean {
        return Objects.nonNull(getById("contactId", key, Contact::class.java))
    }

    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        if (lock) {
            dataStore?.save(Lock(key, user))
        } else {
            val userLocked = getById("id", key, Lock::class.java)
            if (Objects.isNull(userLocked)) return false
            dataStore?.delete(userLocked)
        }
        return true
    }

    override fun unlockAllRecordsForUser(user: String): String {
        dataStore?.delete(dataStore?.createQuery(Lock::class.java)?.field("login")?.equal(user))
        return "OK"
    }

    override fun getUserByLogin(login: String): User? {
        return getById("login", login, User::class.java)
    }

    override fun clearMenus(): String {
        dataStore?.delete(dataStore?.createQuery(MenuEntry::class.java))
        return "OK"
    }

    override fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryId: String?): MenuEntryDto {
        val menuEntry = if (Objects.nonNull(menuEntryDto.id)) getById("id", menuEntryDto.id, MenuEntry::class.java) else MenuEntry()
        menuEntry?.name = menuEntryDto.name
        menuEntry?.url = menuEntryDto.url
        menuEntry?.roles = menuEntryDto.roles
        if (Objects.nonNull(parentEntryId)) menuEntry?.parentId = parentEntryId
        menuEntryDto.id = menuEntry?.id
        dataStore?.save(menuEntry)
        return menuEntryDto
    }

    private fun checkIfMenuExists(url: String): List<MenuEntry>? {
        val entries = dataStore?.createQuery(MenuEntry::class.java)?.field("url")?.equal(url)?.find()?.toList() as List<MenuEntry>
        if (entries.isEmpty()) throw IllegalArgumentException("Menu with url: $url doesn't exist")
        return entries
    }

    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        val entries = checkIfMenuExists(url)
        val menuEntry = entries?.get(0)
        val menuEntryDtos = ArrayList<MenuEntryDto>()
        val cursor = dataStore?.createQuery(MenuEntry::class.java)?.field("parentId")?.equal(menuEntry?.id)?.find()?.toList() as MutableList<MenuEntry>
        for (e in cursor) {
            for (authority in authorities) {
                if (Objects.nonNull(e.roles) && e.roles!!.contains(authority.replace("ROLE_", ""))) {
                    menuEntryDtos.add(MenuEntryDto(e))
                    break
                }
            }
        }
        return menuEntryDtos
    }

    override fun readBreadcrumbs(url: String): List<Breadcrumb> {
        val entries = checkIfMenuExists(url)
        val original = entries?.get(0)
        var menuEntry = original as MenuEntry
        var menuEntries: MutableList<MenuEntry>
        val breadcrumbs = ArrayList<Breadcrumb>()
        if (Objects.isNull(menuEntry.parentId)) return breadcrumbs
        while (true) {
            menuEntries = dataStore?.createQuery(MenuEntry::class.java)?.field("id")?.equal(menuEntry.parentId)?.find()?.toList() as MutableList<MenuEntry>
            if (menuEntries.isNotEmpty()) {
                menuEntry = menuEntries[0]
                breadcrumbs.add(0, Breadcrumb(menuEntry.name, menuEntry.url))
            } else break
        }
        breadcrumbs.add(Breadcrumb(original.name, original.url))
        return breadcrumbs
    }

    private fun <T : Any> getQuerySql(filterDto: List<FilterDto>, queryBase: Query<T>?): Query<T>? {
        var temp = queryBase
        if (filterDto.isNotEmpty()) {
            for (filter in filterDto) {
                if (filter.name == "street" || filter.name == "zip") {
                    filter.name = "addr.$filter.name"
                }
                val type = filter.type
                if (type.equals("NumberFilter")) {
                    val tempFieldEnd = temp?.field(filter.name)
                    val query = Integer.parseInt(filter.value)
                    when (filter.comparator) {
                        "=" -> temp = tempFieldEnd?.equal(query)
                        ">" -> temp = tempFieldEnd?.greaterThan(query)
                        ">=" -> temp = tempFieldEnd?.greaterThanOrEq(query)
                        "<=" -> temp = tempFieldEnd?.lessThanOrEq(query)
                        "<" -> temp = tempFieldEnd?.lessThan(query)
                    }
                }
                if (type.equals("TextFilter")) temp = temp?.field(filter.name)?.containsIgnoreCase(filter.value)
                if (type.equals("DateFilter")) {
                    filter.value = filter.value?.substring(0, 10)
                    val dateBefore = dateFormatEqual.parse(filter.value + "T00:00:00")
                    val dateAfter = dateFormatEqual.parse(filter.value + "T23:59:59")
                    val dateOther = dateFormatOther.parse(filter.value);
                    when (filter.comparator) {
                        "=" -> {
                            temp = temp?.field(filter.name)
                                    ?.greaterThan(Timestamp(dateBefore.time))
                                    ?.field(filter.name)
                                    ?.lessThan(Timestamp(dateAfter.time))
                        }
                        "!=" -> {
                            temp = temp?.field(filter.name)
                                    ?.lessThan(Timestamp(dateBefore.time))
                                    ?.field(filter.name)
                                    ?.greaterThan(Timestamp(dateAfter.time))
                        }
                        ">" -> {
                            temp = temp?.field(filter.name)
                                    ?.greaterThan(Timestamp(dateOther.time))
                        }
                        ">=" -> {
                            temp = temp?.field(filter.name)
                                    ?.greaterThanOrEq(Timestamp(dateOther.time))
                        }
                        "<=" -> {
                            temp = temp?.field(filter.name)
                                    ?.lessThanOrEq(Timestamp(dateOther.time))
                        }
                        "<" -> {
                            temp = temp?.field(filter.name)
                                    ?.lessThan(Timestamp(dateOther.time))
                        }
                    }
                }
            }
        }
        return temp
    }

    override fun selectCachePage(page: Int, pageSize: Int, sortName: String, sortOrder: String, filterDto: List<FilterDto>, cacheName: String): List<Any> {
        val cacheDtoArrayList = ArrayList<Any>()
        val sortNameNew = if (sortName == "street" || sortName == "zip") {
            "addr.$sortName"
        } else {
            sortName
        }
        var query = dataStore?.createQuery(UniversalFieldsDescriptor.getCacheClass(cacheName))
        query = getQuerySql(filterDto, query)
        query = if (sortOrder == "asc") {
            query?.order(Sort.ascending(sortNameNew))
        } else {
            query?.order(Sort.descending(sortNameNew))
        }
        val cursor = query?.find(FindOptions()
                .skip(((page - 1) * pageSize))
                .limit(pageSize))?.toList()
        val dtoConstructor = UniversalFieldsDescriptor.getDtoClass(cacheName)?.getConstructor(UniversalFieldsDescriptor.getCacheClass(cacheName)) as Constructor
        for (e in cursor!!)
            cacheDtoArrayList.add(dtoConstructor.newInstance(e))
        return cacheDtoArrayList
    }

    override fun getContactsByPersonId(id: String): List<ContactDto> {
        val cacheDtoArrayList = ArrayList<ContactDto>()
        val contacts = dataStore?.createQuery(Contact::class.java)?.field("personId")?.equal(id)?.order(Sort.ascending("type"))?.find()?.toList() as List<Contact>
        for (e in contacts) cacheDtoArrayList.add(ContactDto(e))
        return cacheDtoArrayList
    }

    override fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        return if (filterDto.isEmpty()) {
            dataStore?.createQuery(UniversalFieldsDescriptor.getCacheClass(cacheName))!!.count().toInt()
        } else {
            getQuerySql(filterDto, dataStore?.createQuery(UniversalFieldsDescriptor.getCacheClass(cacheName)))!!.count().toInt()
        }
    }

    @PreDestroy
    fun stopClient() {
        mongoClient?.close()
    }
}
