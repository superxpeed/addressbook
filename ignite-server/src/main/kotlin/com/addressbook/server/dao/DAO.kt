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
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import java.sql.Timestamp
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.cache.Cache
import kotlin.collections.HashSet

@Controller
class DAO : AddressBookDAO {

    private val logger = LoggerFactory.getLogger(DAO::class.java)

    private var ignite: Ignite? = null

    @PostConstruct
    fun startClient() {
        if (ignite != null) return
        val igniteConfiguration = IgniteConfiguration()
        igniteConfiguration.isPeerClassLoadingEnabled = true
        igniteConfiguration.setIncludeEventTypes(org.apache.ignite.events.EventType.EVT_TASK_STARTED,
                org.apache.ignite.events.EventType.EVT_TASK_FINISHED,
                org.apache.ignite.events.EventType.EVT_TASK_FAILED,
                org.apache.ignite.events.EventType.EVT_TASK_TIMEDOUT,
                org.apache.ignite.events.EventType.EVT_TASK_SESSION_ATTR_SET,
                org.apache.ignite.events.EventType.EVT_TASK_REDUCED,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_PUT,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_READ,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_REMOVED)

        val tcpDiscoverySpi = TcpDiscoverySpi()
        val tcpDiscoveryMulticastIpFinder = TcpDiscoveryMulticastIpFinder()
        tcpDiscoveryMulticastIpFinder.setAddresses(Collections.singleton("localhost:47500..47509"))
        tcpDiscoverySpi.ipFinder = tcpDiscoveryMulticastIpFinder
        igniteConfiguration.discoverySpi = tcpDiscoverySpi
        ignite = Ignition.start(igniteConfiguration)
        ignite?.cluster()?.active(true)
        for (cache in FieldDescriptor.getCacheClasses().entries) {
            val cfg = CacheConfiguration<String, Any>()
            cfg.cacheMode = CacheMode.PARTITIONED
            cfg.name = cache.key
            cfg.atomicityMode = CacheAtomicityMode.TRANSACTIONAL
            cfg.isStatisticsEnabled = true
            cfg.writeSynchronizationMode = CacheWriteSynchronizationMode.FULL_SYNC
            cfg.setIndexedTypes(String::class.java, cache.value)
            val createdCache: IgniteCache<String, Any>? = ignite?.getOrCreateCache(cfg)
            if (ignite?.cluster()?.forDataNodes(createdCache?.name)?.nodes()?.isEmpty() as Boolean) {
                logger.info("")
                logger.info(">>> Please start at least 1 remote cache node.")
                logger.info("")
            }
        }
    }

    override fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        val cacheOrganization: IgniteCache<String, Organization>? = ignite?.getOrCreateCache(FieldDescriptor.ORGANIZATION_CACHE)
        var organization = cacheOrganization?.get(organizationDto.id)
        if (organization == null) {
            organization = Organization(organizationDto)
        }
        organization.type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
        organization.lastUpdated = Timestamp(System.currentTimeMillis())
        organization.name = organizationDto.name
        organization.addr = Address(organizationDto.street, organizationDto.zip)
        cacheOrganization?.put(organization.id, organization)
        return organizationDto
    }

    override fun createOrUpdatePerson(personDto: PersonDto, user: String): PersonDto {
        val cachePerson: IgniteCache<String, Person>? = ignite?.getOrCreateCache(FieldDescriptor.PERSON_CACHE)
        var person = if (personDto.id != null) cachePerson?.get(personDto.id) else null
        if (person == null) {
            person = Person()
            personDto.id = person.id
        }
        person.firstName = personDto.firstName
        person.lastName = personDto.lastName
        person.orgId = personDto.orgId
        person.salary = personDto.salary
        person.resume = personDto.resume
        cachePerson?.put(person.id, person)
        return personDto
    }

    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, personId: String): List<ContactDto> {
        if (contactDtos.isEmpty()) return contactDtos
        val cacheContacts: IgniteCache<String, Contact>? = ignite?.getOrCreateCache(FieldDescriptor.CONTACT_CACHE)
        var toDelete = getContactsByPersonId(personId).mapNotNull { it.id }
        val updated = contactDtos.mapNotNull { it.id }
        toDelete = toDelete.minus(updated)
        val transactions = ignite?.transactions()
        val tx = transactions?.txStart()
        for (contactDto in contactDtos) {
            contactDto.personId = personId
            var contact = cacheContacts?.get(contactDto.id)
            if (contact == null) contact = Contact()
            contact.data = contactDto.data
            contact.description = contactDto.description
            contact.personId = contactDto.personId
            contact.type = ContactType.values()[Integer.parseInt(contactDto.type)]
            cacheContacts?.put(contact.contactId, contact)
        }
        for (id in toDelete) cacheContacts?.remove(id)
        tx?.commit()
        tx?.close()
        return contactDtos
    }

    override fun createOrUpdateUser(newUser: User): String {
        val cacheUser: IgniteCache<String, User>? = ignite?.getOrCreateCache(FieldDescriptor.USER_CACHE)
        var user = cacheUser?.get(newUser.login)
        if (user != null) {
            user.password = newUser.password
            user.roles = newUser.roles
        } else user = newUser
        cacheUser?.put(user.login, user)
        return "OK"
    }

    override fun notLockedByUser(key: String, user: String): Boolean {
        val cacheLocks: IgniteCache<String, Lock>? = ignite?.getOrCreateCache(FieldDescriptor.LOCK_RECORD_CACHE)
        val userLocked = cacheLocks?.get(key) ?: return false
        return user != userLocked.login
    }

    override fun ifOrganizationExists(key: String): Boolean {
        val cacheOrganization: IgniteCache<String, Organization>? = ignite?.getOrCreateCache(FieldDescriptor.ORGANIZATION_CACHE)
        return cacheOrganization?.get(key) != null
    }

    override fun ifPersonExists(key: String): Boolean {
        val cachePerson: IgniteCache<String, Person>? = ignite?.getOrCreateCache(FieldDescriptor.PERSON_CACHE)
        return cachePerson?.get(key) != null
    }

    override fun ifContactExists(key: String): Boolean {
        val cacheContacts: IgniteCache<String, Contact>? = ignite?.getOrCreateCache(FieldDescriptor.CONTACT_CACHE)
        return cacheContacts?.get(key) != null
    }

    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        val cacheLocks: IgniteCache<String, Lock>? = ignite?.getOrCreateCache(FieldDescriptor.LOCK_RECORD_CACHE)
        return if (lock) cacheLocks?.putIfAbsent(key, Lock(key, user)) as Boolean else cacheLocks?.remove(key, Lock(key, user)) as Boolean
    }

    override fun unlockAllRecordsForUser(user: String): String {
        val cacheLocks: IgniteCache<String, Lock>? = ignite?.getOrCreateCache(FieldDescriptor.LOCK_RECORD_CACHE)
        cacheLocks?.removeAll(HashSet(cacheLocks.query(ScanQuery { _, v -> v.login == user }, Cache.Entry<String, Lock>::getKey).all))
        return "OK"
    }

    override fun getUserByLogin(login: String): User? {
        val cacheUser: IgniteCache<String, User>? = ignite?.getOrCreateCache(FieldDescriptor.USER_CACHE)
        return cacheUser?.get(login)
    }

    override fun clearMenus(): String {
        val cacheMenu: IgniteCache<String, MenuEntry>? = ignite?.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        cacheMenu?.clear()
        return "OK"
    }

    override fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryId: String?): MenuEntryDto {
        val cachePerson: IgniteCache<String, MenuEntry>? = ignite?.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        val menuEntry: MenuEntry?
        menuEntry = if (menuEntryDto.id != null) cachePerson?.get(menuEntryDto.id) else MenuEntry()
        menuEntry?.name = menuEntryDto.name
        menuEntry?.url = menuEntryDto.url
        menuEntry?.roles = menuEntryDto.roles
        if (parentEntryId != null) menuEntry?.parentId = parentEntryId
        menuEntryDto.id = menuEntry?.id
        cachePerson?.put(menuEntry?.id, menuEntry)
        return menuEntryDto
    }

    private fun checkIfMenuExists(menuCache: IgniteCache<String, MenuEntry>?, url: String): List<Cache.Entry<String, MenuEntry>>? {
        val entries = menuCache?.query(SqlQuery<String, MenuEntry>(MenuEntry::class.java, "url = ?").setArgs(url))?.all
        if (entries != null && entries.isEmpty()) throw IllegalArgumentException("Menu with url: $url doesn't exist")
        return entries
    }

    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        val cache: IgniteCache<String, MenuEntry>? = ignite?.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        val entries = checkIfMenuExists(cache, url)
        val menuEntry = entries?.get(0)?.value
        val sql = SqlQuery<String, MenuEntry>(MenuEntry::class.java, "parentId = ?")
        val menuEntryDtos = ArrayList<MenuEntryDto>()
        val cursor = cache?.query(sql.setArgs(menuEntry?.id))
        if (cursor != null) {
            for (e in cursor) {
                for (authority in authorities) {
                    if (e.value.roles != null && e.value.roles!!.contains(authority.replace("ROLE_", ""))) {
                        menuEntryDtos.add(MenuEntryDto(e.value))
                        break
                    }
                }
            }
        }
        cursor?.close()
        return menuEntryDtos
    }

    override fun readBreadcrumbs(url: String): List<BreadcrumbDto> {
        val cache: IgniteCache<String, MenuEntry>? = ignite?.getOrCreateCache(FieldDescriptor.MENU_CACHE)
        val entries = checkIfMenuExists(cache, url)
        val original = entries?.get(0)?.value
        var menuEntry = original
        var menuEntries: List<Cache.Entry<String, MenuEntry>>
        val breadcrumbs = ArrayList<BreadcrumbDto>()
        if (menuEntry?.parentId == null) return breadcrumbs
        val sql = SqlQuery<String, MenuEntry>(MenuEntry::class.java, "id = ?")
        while (true) {
            menuEntries = cache?.query(sql.setArgs(menuEntry?.parentId))?.all as List<Cache.Entry<String, MenuEntry>>
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
            for (filter in filterDto) {
                val type = filter.type
                var addSql = ""
                if (type.equals("NumberFilter")) {
                    val query = Integer.parseInt(filter.value)
                    addSql = filter.name + getComparator(filter) + query
                }
                if (type.equals("TextFilter")) addSql = filter.name + " like '%" + filter.value?.replace("'", "''") + "%'"
                if (type.equals("DateFilter")) addSql = filter.name + getComparator(filter) + "'" + filter.value + "'"
                if (filterDto.indexOf(filter) == 0) baseSql.append(" where ")
                baseSql.append(addSql)
                if (filterDto.indexOf(filter) != (filterDto.size - 1)) baseSql.append(" and ")
            }
        }
        return baseSql
    }

    override fun selectCachePage(page: Int, pageSize: Int, sortName: String, sortOrder: String, filterDto: List<FilterDto>, cacheName: String): List<Any> {
        val cache: IgniteCache<String, Any>? = ignite?.getOrCreateCache(cacheName)
        val cacheDtoArrayList = ArrayList<Any>()
        val sql = SqlQuery<String, Any>(FieldDescriptor.getCacheClass(cacheName), getQuerySql(filterDto)
                .append(" order by ")
                .append(sortName).append(" ")
                .append(sortOrder)
                .append(" limit ? offset ?")
                .toString())
        val cursor = cache?.query(sql.setArgs(pageSize, (page - 1) * pageSize))
        val dtoConstructor = FieldDescriptor.getDtoClass(cacheName)?.getConstructor(FieldDescriptor.getCacheClass(cacheName))
        if (cursor != null) {
            for (e in cursor)
                dtoConstructor?.newInstance(e.value)?.let { cacheDtoArrayList.add(it) }
        }
        cursor?.close()
        return cacheDtoArrayList
    }

    override fun getContactsByPersonId(id: String): List<ContactDto> {
        val cache: IgniteCache<String, Contact>? = ignite?.getOrCreateCache(FieldDescriptor.CONTACT_CACHE)
        val cacheDtoArrayList = ArrayList<ContactDto>()
        val sql = SqlQuery<String, Contact>(Contact::class.java, "personId = ? order by type")
        val cursor = cache?.query(sql.setArgs(id))
        if (cursor != null) {
            for (e in cursor) cacheDtoArrayList.add(ContactDto(e.value))
        }
        cursor?.close()
        return cacheDtoArrayList
    }

    private fun getComparator(filterDto: FilterDto): String {
        return if (filterDto.comparator == null || filterDto.comparator.equals(""))
            " = "
        else " " + filterDto.comparator + " "
    }

    override fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        if (filterDto.isEmpty()) {
            val cache: IgniteCache<Any, Any>? = ignite?.getOrCreateCache(cacheName)
            return cache?.size(CachePeekMode.ALL) as Int
        }
        val cache: IgniteCache<String, Any>? = ignite?.getOrCreateCache(cacheName)
        val sql = SqlQuery<String, Any>(FieldDescriptor.getCacheClass(cacheName), getQuerySql(filterDto).toString())
        val cursor = cache?.query(sql)
        val size = cursor?.all?.size as Int
        cursor.close()
        return size
    }

    @PreDestroy
    fun stopClient() {
        ignite?.close()
    }
}
