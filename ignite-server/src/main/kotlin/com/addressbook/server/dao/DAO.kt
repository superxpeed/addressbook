@file:Suppress("DEPRECATION")

package com.addressbook.server.dao

import com.addressbook.AddressBookDAO
import com.addressbook.FieldDescriptor
import com.addressbook.dto.*
import com.addressbook.model.*
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.Ignition
import org.apache.ignite.cache.CacheAtomicityMode
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.cache.CachePeekMode
import org.apache.ignite.cache.CacheWriteSynchronizationMode
import org.apache.ignite.cache.query.ScanQuery
import org.apache.ignite.cache.query.SqlQuery
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.events.EventType
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import java.sql.Timestamp
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.cache.Cache

@Controller
class DAO : AddressBookDAO {

    private val logger = LoggerFactory.getLogger(DAO::class.java)

    private lateinit var ignite: Ignite

    @PostConstruct
    fun startClient() {
        val igniteConfiguration = IgniteConfiguration()
        with(igniteConfiguration) {
            isPeerClassLoadingEnabled = true
            setIncludeEventTypes(EventType.EVT_TASK_STARTED,
                    EventType.EVT_TASK_FINISHED,
                    EventType.EVT_TASK_FAILED,
                    EventType.EVT_TASK_TIMEDOUT,
                    EventType.EVT_TASK_SESSION_ATTR_SET,
                    EventType.EVT_TASK_REDUCED,
                    EventType.EVT_CACHE_OBJECT_PUT,
                    EventType.EVT_CACHE_OBJECT_READ,
                    EventType.EVT_CACHE_OBJECT_REMOVED)
            discoverySpi = TcpDiscoverySpi()
                    .also { spi ->
                        spi.ipFinder = TcpDiscoveryMulticastIpFinder()
                                .also { it.setAddresses(Collections.singleton("localhost:47500..47509")) }
                    }
        }
        ignite = Ignition.start(igniteConfiguration)
        ignite.cluster()?.active(true)
        FieldDescriptor.getCacheClasses().entries.forEach {
            val cfg = CacheConfiguration<String, Any>()
            with(cfg) {
                cacheMode = CacheMode.PARTITIONED
                name = it.key
                atomicityMode = CacheAtomicityMode.TRANSACTIONAL
                isStatisticsEnabled = true
                writeSynchronizationMode = CacheWriteSynchronizationMode.FULL_SYNC
                setIndexedTypes(String::class.java, it.value)
            }
            val createdCache: IgniteCache<String, Any>? = ignite.getOrCreateCache(cfg)
            if (ignite.cluster()?.forDataNodes(createdCache?.name)?.nodes()?.isEmpty() as Boolean) {
                logger.info("")
                logger.info(">>> Please start at least 1 remote cache node.")
                logger.info("")
            }
        }
    }

    override fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        requireNotNull(organizationDto.id)
        val cacheOrganization: IgniteCache<String, Organization>? = ignite.getOrCreateCache(FieldDescriptor.ORGANIZATION_CACHE)
        val organization = cacheOrganization?.get(organizationDto.id) ?: Organization(organizationDto)
        with(organization) {
            type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
            lastUpdated = Timestamp(System.currentTimeMillis())
            name = organizationDto.name
            addr = Address(organizationDto.street, organizationDto.zip)
        }
        organizationDto.lastUpdated = dateFormatter.format(organization.lastUpdated)
        cacheOrganization?.put(organization.id, organization)
        return organizationDto
    }

    override fun getOrganizationById(id: String): OrganizationDto? {
        val cacheOrganization: IgniteCache<String, Organization>? = ignite.getOrCreateCache(FieldDescriptor.ORGANIZATION_CACHE)
        val organization = cacheOrganization?.get(id) ?: return null
        return OrganizationDto(organization)
    }

    override fun createOrUpdatePerson(personDto: PersonDto): PersonDto {
        val cachePerson: IgniteCache<String, Person>? = ignite.getOrCreateCache(FieldDescriptor.PERSON_CACHE)
        val person = personDto.id?.let { cachePerson?.get(personDto.id) } ?: Person().also { personDto.id = it.id }
        with(person) {
            firstName = personDto.firstName
            lastName = personDto.lastName
            orgId = personDto.orgId
            salary = personDto.salary
            resume = personDto.resume
        }
        cachePerson?.put(person.id, person)
        return personDto
    }

    override fun getPersonById(id: String): PersonDto? {
        val cachePerson: IgniteCache<String, Person>? = ignite.getOrCreateCache(FieldDescriptor.PERSON_CACHE)
        val person = cachePerson?.get(id) ?: return null
        return PersonDto(person)
    }

    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, targetPersonId: String): List<ContactDto> {
        val cacheContacts: IgniteCache<String, Contact>? = ignite.getOrCreateCache(FieldDescriptor.CONTACT_CACHE)
        val toDelete = getContactsByPersonId(targetPersonId).mapNotNull { it.id }.minus(contactDtos.mapNotNull { it.id }.toSet())
        val tx = ignite.transactions()?.txStart()
        tx.use {
            contactDtos.forEach {
                it.personId = targetPersonId
                val contact = if (it.id == null) Contact() else cacheContacts?.get(it.id) ?: Contact()
                with(contact) {
                    data = it.data
                    description = it.description
                    personId = it.personId
                    type = ContactType.values()[Integer.parseInt(it.type)]
                }
                cacheContacts?.put(contact.contactId, contact)
                it.id = contact.contactId
            }
            toDelete.forEach { cacheContacts?.remove(it) }
            tx?.commit()
        }
        return contactDtos
    }

    override fun createOrUpdateUser(newUser: User) {
        val cacheUser: IgniteCache<String, User>? = ignite.getOrCreateCache(FieldDescriptor.USER_CACHE)
        var user = cacheUser?.get(newUser.login)
        user?.let {
            it.password = newUser.password
            it.roles = newUser.roles
        } ?: let { user = newUser }
        cacheUser?.put(user?.login, user)
    }

    override fun notLockedByUser(key: String, user: String): Boolean {
        val cacheLocks: IgniteCache<String, Lock>? = ignite.getOrCreateCache(FieldDescriptor.LOCK_RECORD_CACHE)
        val userLocked = cacheLocks?.get(key) ?: return false
        return user != userLocked.login
    }

    override fun ifOrganizationExists(key: String?): Boolean {
        val cacheOrganization: IgniteCache<String, Organization>? = ignite.getOrCreateCache(FieldDescriptor.ORGANIZATION_CACHE)
        return if (key == null)
            false
        else cacheOrganization?.get(key) != null
    }

    override fun ifPersonExists(key: String?): Boolean {
        val cachePerson: IgniteCache<String, Person>? = ignite.getOrCreateCache(FieldDescriptor.PERSON_CACHE)
        return if (key == null)
            false
        else cachePerson?.get(key) != null
    }

    override fun ifContactExists(key: String?): Boolean {
        val cacheContacts: IgniteCache<String, Contact>? = ignite.getOrCreateCache(FieldDescriptor.CONTACT_CACHE)
        return if (key == null)
            false
        else cacheContacts?.get(key) != null
    }

    override fun ifPageExists(page: String): Boolean {
        val cache: IgniteCache<String, MenuEntry>? = ignite.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        return getMenuEntriesByUrl(cache, page).isNotEmpty()
    }

    override fun saveDocument(document: DocumentDto) {
        val cache: IgniteCache<String, Document>? = ignite.getOrCreateCache(FieldDescriptor.DOCUMENT_CACHE)
        val tx = ignite.transactions()?.txStart()
        tx.use {
            val targetDocument = Document()
            targetDocument.id = document.id
            targetDocument.name = document.name
            targetDocument.checksum = document.checksum
            targetDocument.personId = document.personId
            targetDocument.size = document.size
            cache?.put(targetDocument.id, targetDocument)
            tx?.commit()
        }
    }

    override fun deleteDocument(id: String) {
        val cache: IgniteCache<String, Document>? = ignite.getOrCreateCache(FieldDescriptor.DOCUMENT_CACHE)
        val tx = ignite.transactions()?.txStart()
        tx.use {
            cache?.remove(id)
            tx?.commit()
        }
    }

    override fun getDocumentsByPersonId(id: String): List<DocumentDto> {
        val cache: IgniteCache<String, Document>? = ignite.getOrCreateCache(FieldDescriptor.DOCUMENT_CACHE)
        val cacheDtoArrayList = ArrayList<DocumentDto>()
        val cursor = cache?.query(SqlQuery<String, Document>(Document::class.java, "personId = ? order by createDate asc").setArgs(id))
        cursor.use { cursor?.forEach { cacheDtoArrayList.add(DocumentDto(it.value)) } }
        return cacheDtoArrayList
    }

    override fun getDocumentById(id: String): DocumentDto? {
        val cache: IgniteCache<String, Document>? = ignite.getOrCreateCache(FieldDescriptor.DOCUMENT_CACHE)
        val document = cache?.get(id) ?: return null
        return DocumentDto(document)
    }

    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        val cacheLocks: IgniteCache<String, Lock>? = ignite.getOrCreateCache(FieldDescriptor.LOCK_RECORD_CACHE)
        if (lock) {
            val userLocked = cacheLocks?.get(key)
            return if (userLocked == null) {
                cacheLocks?.put(key, Lock(key, user))
                true
            } else return user.lowercase() == userLocked.login?.lowercase()
        } else {
            val userLocked = cacheLocks?.get(key) ?: return true
            return if (user.lowercase() == userLocked.login?.lowercase()) {
                cacheLocks.remove(key, Lock(key, user))
                true
            } else
                false
        }
    }

    override fun unlockAllRecordsForUser(user: String) {
        val cacheLocks: IgniteCache<String, Lock>? = ignite.getOrCreateCache(FieldDescriptor.LOCK_RECORD_CACHE)
        cacheLocks?.removeAll(HashSet(cacheLocks.query(ScanQuery { _, v -> v.login == user }, Cache.Entry<String, Lock>::getKey).all))
    }

    override fun getUserByLogin(login: String): User? {
        val cacheUser: IgniteCache<String, User>? = ignite.getOrCreateCache(FieldDescriptor.USER_CACHE)
        return cacheUser?.get(login)
    }

    override fun clearMenus() {
        val cacheMenu: IgniteCache<String, MenuEntry>? = ignite.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        cacheMenu?.clear()
    }

    override fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryId: String?): MenuEntryDto {
        val cachePerson: IgniteCache<String, MenuEntry>? = ignite.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        val menuEntry = menuEntryDto.id?.let { cachePerson?.get(menuEntryDto.id) } ?: MenuEntry()
        with(menuEntry) {
            name = menuEntryDto.name
            url = menuEntryDto.url
            roles = menuEntryDto.roles
            parentEntryId?.let { menuEntry.parentId = parentEntryId }
        }
        menuEntryDto.id = menuEntry.id
        cachePerson?.put(menuEntry.id, menuEntry)
        return menuEntryDto
    }

    private fun getMenuEntriesByUrl(menuCache: IgniteCache<String, MenuEntry>?, url: String): List<Cache.Entry<String, MenuEntry>> {
        return menuCache?.query(SqlQuery<String, MenuEntry>(MenuEntry::class.java, "url = ?").setArgs(url))?.all
                ?: emptyList()
    }

    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        val cache: IgniteCache<String, MenuEntry>? = ignite.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        val entries = getMenuEntriesByUrl(cache, url)
        if (entries.isEmpty()) return emptyList()
        val menuEntries = ArrayList<MenuEntryDto>()
        val cursor = cache?.query(SqlQuery<String, MenuEntry>(MenuEntry::class.java, "parentId = ?").setArgs(entries[0].value?.id))
        cursor.use {
            cursor?.forEach { e ->
                for (authority in authorities) {
                    if (e.value.roles != null && e.value.roles!!.contains(authority.replace("ROLE_", ""))) {
                        menuEntries.add(MenuEntryDto(e.value))
                        break
                    }
                }
            }
        }
        return menuEntries
    }

    override fun readBreadcrumbs(url: String): List<BreadcrumbDto> {
        val cache: IgniteCache<String, MenuEntry>? = ignite.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        val entries = getMenuEntriesByUrl(cache, url)
        if (entries.isEmpty()) return emptyList()
        val original = entries[0].value
        var menuEntry = original
        val breadcrumbs = ArrayList<BreadcrumbDto>()
        if (menuEntry?.parentId == null) return breadcrumbs
        while (true) {
            val menuEntries = cache?.query(SqlQuery<String, MenuEntry>(MenuEntry::class.java, "id = ?")
                    .setArgs(menuEntry?.parentId))?.all as List<Cache.Entry<String, MenuEntry>>
            if (menuEntries.isNotEmpty()) {
                menuEntry = menuEntries[0].value
                breadcrumbs.add(0, BreadcrumbDto(menuEntry.name, menuEntry.url))
            } else break
        }
        breadcrumbs.add(BreadcrumbDto(original?.name, original?.url))
        return breadcrumbs
    }

    private fun getQuerySql(filterDto: List<FilterDto>): StringBuilder {
        val baseSql = StringBuilder(" ")
        if (filterDto.isNotEmpty()) {
            filterDto.forEach {
                var addSql = ""
                when (it.type) {
                    "NumberFilter" -> addSql = it.name + getComparator(it) + Integer.parseInt(it.value)
                    "TextFilter" -> if (it.name == "type") {
                        it.value?.let { typeOrdinal ->
                            if (typeOrdinal.isNotBlank() && typeOrdinal.toInt() >= 0 && typeOrdinal.toInt() < OrganizationType.values().size)
                                addSql = it.name + " like '%" + OrganizationType.values()[typeOrdinal.toInt()].name + "%'"
                        }
                    } else {
                        addSql = it.name + " like '%" + it.value?.replace("'", "''") + "%'"
                    }

                    "DateFilter" -> {
                        it.value = it.value?.substring(0, 10)
                        val tailLower = " 00:00:00.000'"
                        val tailUpper = " 23:59:59.999'"
                        val format = "'yyyy-MM-dd HH:ss:SSS'"
                        when (it.comparator) {
                            "=" -> {
                                addSql = it.name + " BETWEEN PARSEDATETIME('" + it.value + tailLower + " , " + format + ") AND PARSEDATETIME('" + it.value + tailUpper + " , " + format + ")"
                            }

                            "!=" -> {
                                addSql = it.name + " < PARSEDATETIME('" + it.value + tailLower + " , " + format + ") OR " + it.name + " > PARSEDATETIME('" + it.value + tailUpper + " , " + format + ")"
                            }

                            ">" -> {
                                addSql = it.name + " > PARSEDATETIME('" + it.value + tailUpper + " , " + format + ")"
                            }

                            ">=" -> {
                                addSql = it.name + " > PARSEDATETIME('" + it.value + tailLower + " , " + format + ")"
                            }

                            "<=" -> {
                                addSql = it.name + " < PARSEDATETIME('" + it.value + tailUpper + " , " + format + ")"
                            }

                            "<" -> {
                                addSql = it.name + " < PARSEDATETIME('" + it.value + tailLower + " , " + format + ")"
                            }
                        }
                    }
                }
                if (filterDto.indexOf(it) == 0) baseSql.append(" where ")
                baseSql.append(addSql)
                if (filterDto.indexOf(it) != (filterDto.size - 1)) baseSql.append(" and ")
            }
        }
        return baseSql
    }

    override fun selectCachePage(page: Int, pageSize: Int, sortName: String, sortOrder: String, filterDto: List<FilterDto>, cacheName: String): List<Any> {
        val cache: IgniteCache<String, Any>? = ignite.getOrCreateCache(cacheName)
        val cacheDtoArrayList = ArrayList<Any>()
        val cursor = cache?.query(SqlQuery<String, Any>(FieldDescriptor.getCacheClass(cacheName),
                getQuerySql(filterDto)
                        .append(" order by ")
                        .append(sortName)
                        .append(" ")
                        .append(sortOrder)
                        .append(" limit ? offset ?").toString())
                .setArgs(pageSize, (page - 1) * pageSize))
        val dtoConstructor = FieldDescriptor.getDtoClass(cacheName)?.getConstructor(FieldDescriptor.getCacheClass(cacheName))
        cursor.use { cursor?.forEach { x -> dtoConstructor?.newInstance(x.value)?.let { cacheDtoArrayList.add(it) } } }
        return cacheDtoArrayList
    }

    override fun getContactsByPersonId(id: String): List<ContactDto> {
        val cache: IgniteCache<String, Contact>? = ignite.getOrCreateCache(FieldDescriptor.CONTACT_CACHE)
        val cacheDtoArrayList = ArrayList<ContactDto>()
        val cursor = cache?.query(SqlQuery<String, Contact>(Contact::class.java, "personId = ? order by createDate asc").setArgs(id))
        cursor.use { cursor?.forEach { cacheDtoArrayList.add(ContactDto(it.value)) } }
        return cacheDtoArrayList
    }

    private fun getComparator(filterDto: FilterDto): String {
        return if (filterDto.comparator == null || filterDto.comparator.equals("")) " = "
        else " " + filterDto.comparator + " "
    }

    override fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        if (filterDto.isEmpty()) {
            val cache: IgniteCache<Any, Any>? = ignite.getOrCreateCache(cacheName)
            return cache?.size(CachePeekMode.ALL) as Int
        }
        val cache: IgniteCache<String, Any>? = ignite.getOrCreateCache(cacheName)
        val cursor = cache?.query(SqlQuery<String, Any>(FieldDescriptor.getCacheClass(cacheName), getQuerySql(filterDto).toString()))
        var size: Int
        cursor.use { size = cursor?.all?.size as Int }
        return size
    }

    @PreDestroy
    fun stopClient() {
        ignite.close()
    }
}
