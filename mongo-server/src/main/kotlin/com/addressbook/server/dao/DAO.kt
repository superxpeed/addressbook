package com.addressbook.server.dao

import com.addressbook.AddressBookDAO
import com.addressbook.FieldDescriptor
import com.addressbook.dto.*
import com.addressbook.model.*
import com.mongodb.*
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.Sort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import java.io.FileInputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.net.ssl.*


@Controller
@Suppress("DEPRECATION")
class DAO : AddressBookDAO {

    private val dateFormatEqual = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    private lateinit var mongoClient: MongoClient
    private lateinit var dataStore: Datastore

    @Autowired
    lateinit var env: Environment

    @PostConstruct
    fun startClient() {
        val credentials = MongoCredential.createScramSha1Credential(
                requireNotNull(env.getProperty("mongo.user")),
                "addressbook",
                requireNotNull(env.getProperty("mongo.password")).toCharArray())
        val clientStore = KeyStore.getInstance("PKCS12")
        clientStore.load(FileInputStream(
                requireNotNull(env.getProperty("mongo.keystore.path"))),
                requireNotNull(env.getProperty("mongo.keystore.password")).toCharArray())
        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(clientStore, requireNotNull(env.getProperty("mongo.keystore.key-password")).toCharArray())
        val trustStore = KeyStore.getInstance("PKCS12")
        trustStore.load(FileInputStream(
                requireNotNull(env.getProperty("mongo.truststore.path"))),
                requireNotNull(env.getProperty("mongo.truststore.password")).toCharArray())
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(trustStore)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(kmf.keyManagers, tmf.trustManagers, SecureRandom())

        mongoClient = MongoClient(ServerAddress(requireNotNull(env.getProperty("mongo.host")),
                Integer.parseInt(requireNotNull(env.getProperty("mongo.port")))),
                credentials, MongoClientOptions.builder()
                .sslEnabled(true)
                .sslContext(sslContext)
                .build())
        dataStore = Morphia()
                .also { it.mapPackage("com.addressbook.model") }
                .createDatastore(mongoClient, "addressbook")
                .also { it.ensureIndexes() }
    }

    private fun <T : Any> getById(idColumn: String, id: String?, clazz: Class<T>): T? {
        val collection = dataStore.createQuery(clazz)
                ?.field(idColumn)
                ?.equal(id)
                ?.find()
                ?.toList() as List<T>
        return if (collection.isNotEmpty()) collection[0] else null
    }

    override fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        requireNotNull(organizationDto.id)
        requireNotNull(organizationDto.type)
        val organization = getById("id", organizationDto.id, Organization::class.java) ?: Organization(organizationDto)
        with(organization) {
            type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
            lastUpdated = Timestamp(System.currentTimeMillis())
            name = organizationDto.name
            addr = Address(organizationDto.street, organizationDto.zip)
        }
        organizationDto.lastUpdated = dateFormatter.format(organization.lastUpdated)
        dataStore.save(organization)
        return organizationDto
    }

    override fun getOrganizationById(id: String): OrganizationDto? {
        val organization = getById("id", id, Organization::class.java) ?: return null
        return OrganizationDto(organization)
    }

    override fun createOrUpdatePerson(personDto: PersonDto): PersonDto {
        val person = personDto.id?.let { getById("id", personDto.id, Person::class.java) }
                ?: Person().also { personDto.id = it.id }
        with(person) {
            firstName = personDto.firstName
            lastName = personDto.lastName
            orgId = personDto.orgId
            salary = personDto.salary
            resume = personDto.resume
        }
        dataStore.save(person)
        return personDto
    }

    override fun getPersonById(id: String): PersonDto? {
        val person = getById("id", id, Person::class.java) ?: return null
        return PersonDto(person)
    }

    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, targetPersonId: String): List<ContactDto> {
        val toDelete = getContactsByPersonId(targetPersonId).mapNotNull { it.id }.minus(contactDtos.mapNotNull { it.id }.toSet())
        contactDtos.forEach {
            it.personId = targetPersonId
            val contact = if (it.id.isNullOrBlank())
                Contact()
            else
                getById("contactId", it.id, Contact::class.java) ?: Contact()
            with(contact) {
                data = it.data
                description = it.description
                personId = it.personId
                type = ContactType.values()[Integer.parseInt(it.type)]
            }
            dataStore.save(contact)
            it.id = contact.contactId
        }
        toDelete.forEach { dataStore.delete(getById("contactId", it, Contact::class.java)) }
        return contactDtos
    }

    override fun createOrUpdateUser(newUser: User) {
        var user = getById("login", newUser.login, User::class.java)
        user?.let {
            it.password = newUser.password
            it.roles = newUser.roles
        } ?: let { user = newUser }
        dataStore.save(user)
    }

    override fun notLockedByUser(key: String, user: String): Boolean {
        val userLocked = getById("id", key, Lock::class.java) ?: return true
        return user != userLocked.login
    }

    override fun ifOrganizationExists(key: String?): Boolean {
        return if (key.isNullOrBlank())
            false
        else getById("id", key, Organization::class.java) != null
    }

    override fun ifPersonExists(key: String?): Boolean {
        return if (key.isNullOrBlank())
            false
        else getById("id", key, Person::class.java) != null
    }

    override fun ifContactExists(key: String?): Boolean {
        return if (key.isNullOrBlank())
            false
        else getById("contactId", key, Contact::class.java) != null
    }

    override fun ifPageExists(page: String): Boolean {
        return getMenuEntriesByUrl(page).isNotEmpty()
    }

    override fun saveDocument(document: DocumentDto) {
        val targetDocument = Document()
        targetDocument.id = document.id
        targetDocument.name = document.name
        targetDocument.checksum = document.checksum
        targetDocument.personId = document.personId
        targetDocument.size = document.size
        dataStore.save(targetDocument)
    }

    override fun deleteDocument(id: String) {
        dataStore.delete(getById("id", id, Document::class.java))
    }

    override fun getDocumentsByPersonId(id: String): List<DocumentDto> {
        return dataStore.createQuery(Document::class.java)
                ?.field("personId")
                ?.equal(id)
                ?.order(Sort.ascending("createDate"))
                ?.find()
                ?.toList()
                ?.map { DocumentDto(it) }
                ?.toList()
                ?: emptyList()
    }

    override fun getDocumentById(id: String): DocumentDto? {
        val document = getById("id", id, Document::class.java) ?: return null
        return DocumentDto(document)
    }

    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        if (lock) {
            val userLocked = getById("id", key, Lock::class.java)
            return if (userLocked == null) {
                dataStore.save(Lock(key, user))
                true
            } else return user.lowercase() == userLocked.login?.lowercase()
        } else {
            val userLocked = getById("id", key, Lock::class.java) ?: return true
            return if (user.lowercase() == userLocked.login?.lowercase()) {
                dataStore.delete(userLocked)
                true
            } else
                false
        }
    }

    override fun unlockAllRecordsForUser(user: String) {
        dataStore.delete(dataStore.createQuery(Lock::class.java)
                ?.field("login")
                ?.equal(user))
    }

    override fun getUserByLogin(login: String): User? {
        return getById("login", login, User::class.java)
    }

    override fun clearMenus() {
        dataStore.delete(dataStore.createQuery(MenuEntry::class.java))
    }

    override fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryId: String?): MenuEntryDto {
        val menuEntry = menuEntryDto.id?.let { getById("id", menuEntryDto.id, MenuEntry::class.java) } ?: MenuEntry()
        with(menuEntry) {
            name = menuEntryDto.name
            url = menuEntryDto.url
            roles = menuEntryDto.roles
            parentEntryId?.let { menuEntry.parentId = parentEntryId }
        }
        menuEntryDto.id = menuEntry.id
        dataStore.save(menuEntry)
        return menuEntryDto
    }

    private fun getMenuEntriesByUrl(url: String): List<MenuEntry> {
        return dataStore.createQuery(MenuEntry::class.java)
                ?.field("url")
                ?.equal(url)
                ?.find()
                ?.toList() ?: emptyList()
    }

    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        val entries = getMenuEntriesByUrl(url)
        if (entries.isEmpty()) return emptyList()
        val menuEntries = ArrayList<MenuEntryDto>()
        dataStore.createQuery(MenuEntry::class.java)
                ?.field("parentId")
                ?.equal(entries[0].id)
                ?.find()
                ?.toList()
                ?.forEach { e ->
                    for (authority in authorities) {
                        if (e.roles != null && e.roles!!.contains(authority.replace("ROLE_", ""))) {
                            menuEntries.add(MenuEntryDto(e))
                            break
                        }
                    }
                }
        return menuEntries
    }

    override fun readBreadcrumbs(url: String): List<BreadcrumbDto> {
        val entries = getMenuEntriesByUrl(url)
        if (entries.isEmpty()) return emptyList()
        val original = entries[0]
        var menuEntry = original
        if (menuEntry.parentId.isNullOrBlank()) {
            return listOf(BreadcrumbDto(original.name, original.url))
        }
        val breadcrumbs = ArrayList<BreadcrumbDto>()
        if (menuEntry.parentId.isNullOrBlank()) return breadcrumbs
        while (true) {
            val menuEntries = dataStore.createQuery(MenuEntry::class.java)
                    ?.field("id")
                    ?.equal(menuEntry.parentId)
                    ?.find()
                    ?.toList() as MutableList<MenuEntry>
            if (menuEntries.isNotEmpty()) {
                menuEntry = menuEntries[0]
                breadcrumbs.add(0, BreadcrumbDto(menuEntry.name, menuEntry.url))
            } else break
        }
        breadcrumbs.add(BreadcrumbDto(original.name, original.url))
        return breadcrumbs
    }

    private fun <T : Any> getQuerySql(filterDto: List<FilterDto>, queryBase: Query<T>?): Query<T>? {
        var temp = queryBase
        if (filterDto.isNotEmpty()) {
            filterDto.forEach {
                if (it.name == "street" || it.name == "zip") {
                    it.name = "addr." + it.name
                }
                when (it.type) {
                    "NumberFilter" -> {
                        val tempFieldEnd = temp?.field(it.name)
                        val query = Integer.parseInt(it.value)
                        when (it.comparator) {
                            "=" -> temp = tempFieldEnd?.equal(query)
                            ">" -> temp = tempFieldEnd?.greaterThan(query)
                            ">=" -> temp = tempFieldEnd?.greaterThanOrEq(query)
                            "<=" -> temp = tempFieldEnd?.lessThanOrEq(query)
                            "<" -> temp = tempFieldEnd?.lessThan(query)
                        }
                    }

                    "TextFilter" -> if (it.name == "type") {
                        it.value?.let { typeOrdinal ->
                            if (typeOrdinal.isNotBlank() && typeOrdinal.toInt() >= 0 && typeOrdinal.toInt() < OrganizationType.values().size)
                                temp = temp?.field(it.name)?.containsIgnoreCase(OrganizationType.values()[typeOrdinal.toInt()].name)
                        }
                    } else {
                        temp = temp?.field(it.name)?.containsIgnoreCase(it.value)
                    }

                    "DateFilter" -> {
                        it.value = it.value?.substring(0, 10)
                        val dateBefore = dateFormatEqual.parse(it.value + "T00:00:00")
                        val dateAfter = dateFormatEqual.parse(it.value + "T23:59:59")
                        when (it.comparator) {
                            "=" -> {
                                temp = temp?.field(it.name)?.greaterThan(Timestamp(dateBefore.time))?.field(it.name)?.lessThan(Timestamp(dateAfter.time))
                            }

                            "!=" -> {
                                temp?.or(temp?.criteria(it.name)?.lessThan(Timestamp(dateBefore.time)), temp?.criteria(it.name)?.greaterThan(Timestamp(dateAfter.time)))
                            }

                            ">" -> {
                                temp = temp?.field(it.name)?.greaterThan(Timestamp(dateAfter.time))
                            }

                            ">=" -> {
                                temp = temp?.field(it.name)?.greaterThanOrEq(Timestamp(dateBefore.time))
                            }

                            "<=" -> {
                                temp = temp?.field(it.name)?.lessThanOrEq(Timestamp(dateAfter.time))
                            }

                            "<" -> {
                                temp = temp?.field(it.name)?.lessThan(Timestamp(dateBefore.time))
                            }
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
        val dtoConstructor = FieldDescriptor.getDtoClass(cacheName)?.getConstructor(FieldDescriptor.getCacheClass(cacheName))
        getQuerySql(filterDto, dataStore.createQuery(FieldDescriptor.getCacheClass(cacheName))).let {
            if (sortOrder == "asc") {
                it?.order(Sort.ascending(sortNameNew))
            } else {
                it?.order(Sort.descending(sortNameNew))
            }
        }?.find(FindOptions()
                .skip(((page - 1) * pageSize))
                .limit(pageSize))
                ?.toList()
                ?.forEach { e ->
                    dtoConstructor?.newInstance(e)
                            ?.let { cacheDtoArrayList.add(it) }
                }
        return cacheDtoArrayList
    }

    override fun getContactsByPersonId(id: String): List<ContactDto> {
        return dataStore.createQuery(Contact::class.java)
                ?.field("personId")
                ?.equal(id)
                ?.order(Sort.ascending("createDate"))
                ?.find()
                ?.toList()
                ?.map { ContactDto(it) }
                ?.toList()
                ?: emptyList()
    }

    override fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        return if (filterDto.isEmpty()) {
            dataStore.createQuery(FieldDescriptor.getCacheClass(cacheName))?.count()?.toInt() as Int
        } else {
            getQuerySql(filterDto, dataStore.createQuery(FieldDescriptor.getCacheClass(cacheName)))
                    ?.count()
                    ?.toInt() as Int
        }
    }

    @PreDestroy
    fun stopClient() {
        mongoClient.close()
    }
}
