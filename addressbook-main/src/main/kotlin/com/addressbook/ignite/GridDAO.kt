package com.addressbook.ignite

import com.addressbook.LockRecordException
import com.addressbook.UniversalFieldsDescriptor
import com.addressbook.dto.*
import com.addressbook.model.*
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.Ignition
import org.apache.ignite.cache.*
import org.apache.ignite.cache.query.QueryCursor
import org.apache.ignite.cache.query.ScanQuery
import org.apache.ignite.cache.query.SqlQuery
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority

import javax.cache.Cache
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashSet

object GridDAO {

    private val logger = LoggerFactory.getLogger(GridDAO::class.java)

    var ignite: Ignite? = null

    fun startClient() {
        if (ignite != null) return
        val igniteConfiguration = IgniteConfiguration()
        igniteConfiguration.isPeerClassLoadingEnabled = true
        igniteConfiguration.isClientMode = true
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
        tcpDiscoveryMulticastIpFinder.setAddresses(Collections.singleton("192.168.1.51:47500..47509"))
        tcpDiscoverySpi.ipFinder = tcpDiscoveryMulticastIpFinder
        igniteConfiguration.discoverySpi = tcpDiscoverySpi
        ignite = Ignition.start(igniteConfiguration)
        ignite?.cluster()?.active(true)
        for (cache in UniversalFieldsDescriptor.getCacheClasses().entries) {
            val cfg = CacheConfiguration<String, Any>()
            cfg.cacheMode = CacheMode.PARTITIONED
            cfg.name = cache.key
            cfg.atomicityMode = CacheAtomicityMode.TRANSACTIONAL
            cfg.isStatisticsEnabled = true
            cfg.writeSynchronizationMode = CacheWriteSynchronizationMode.FULL_SYNC
            cfg.setIndexedTypes(String::class.java, cache.value)
            val createdCache: IgniteCache<String, Any>? = ignite?.getOrCreateCache(cfg)
            if (ignite?.cluster()?.forDataNodes(createdCache?.name)?.nodes()?.isEmpty()!!) {
                logger.info("")
                logger.info(">>> Please start at least 1 remote cache node.")
                logger.info("")
            }
        }
        MenuCreator.initMenu()
        UserCreator.initUsers()
    }


    /**
     *7
     * @param organizationDto contains all data related to existing or new organization entity
     * @param user current user (will be used for locking existing entity)
     * @return updated or created organization entity
     */
    fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        val cachePerson: IgniteCache<String, Organization> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.ORGANIZATION_CACHE)!!
        var organization = cachePerson.get(organizationDto.id)
        if (organization == null) {
            organization = Organization(organizationDto)
        } else {
            if (notLockedByUser(Organization::class.java.name + organization.id, user))
                throw LockRecordException("Record was not locked by $user")
        }
        organization.type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
        organization.lastUpdated = Timestamp(System.currentTimeMillis())
        organization.name = organizationDto.name
        organization.addr = Address(organizationDto.street, organizationDto.zip)
        cachePerson.put(organization.id, organization)
        return organizationDto
    }

    /**
     *
     * @param personDto contains all data related to existing or new person entity
     * @param user current user (will be used for locking existing entity)
     * @return updated or created person entity
     */
    fun createOrUpdatePerson(personDto: PersonDto, user: String): PersonDto {
        val cachePerson: IgniteCache<String, Person> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.PERSON_CACHE)!!
        var person = if (personDto.id != null) cachePerson.get(personDto.id) else null
        if (person == null) {
            person = Person()
            personDto.id = person.id
        } else {
            if (notLockedByUser(Person::class.java.name + person.id, user))
                throw LockRecordException("Record was not locked by $user")
        }
        person.firstName = personDto.firstName
        person.lastName = personDto.lastName
        person.orgId = personDto.orgId
        person.salary = personDto.salary
        person.resume = personDto.resume
        cachePerson.put(person.id, person)
        return personDto
    }

    /**
     *
     * @param contactDtos contains all data related to existing or new contact entities
     * @param user current user (was used for locking existing parent person entity)
     * @return updated or created contact entities
     */
    fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, personId: String): List<ContactDto> {
        if (contactDtos.isEmpty()) return contactDtos
        if (notLockedByUser(Person::class.java.name + personId, user))
            throw LockRecordException("Parent record was not locked by $user")
        val cacheContacts: IgniteCache<String, Contact> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.CONTACT_CACHE)!!
        var toDelete: List<String> = getContactsByPersonId(personId).mapNotNull { it.id }
        val updated: List<String> = contactDtos.mapNotNull { it.id }
        toDelete = toDelete.minus(updated)
        val transactions = ignite?.transactions()
        val tx = transactions?.txStart()
        for (contactDto in contactDtos) {
            contactDto.personId = personId
            var contact = cacheContacts.get(contactDto.id)
            if (contact == null) contact = Contact()
            contact.data = contactDto.data
            contact.description = contactDto.description
            contact.personId = contactDto.personId
            contact.type = ContactType.values()[Integer.parseInt(contactDto.type)]
            cacheContacts.put(contact.contactId, contact)
        }
        for (id in toDelete) cacheContacts.remove(id)
        tx?.commit()
        tx?.close()
        return contactDtos
    }

    fun createOrUpdateUser(newUser: User) {
        val cacheUser: IgniteCache<String, User> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.USER_CACHE)!!
        var user = cacheUser.get(newUser.login)
        if (user != null) {
            user.password = newUser.password
            user.roles = newUser.roles
        } else user = newUser
        cacheUser.put(user.login, user)
    }

    private fun notLockedByUser(key: String, user: String): Boolean {
        val cacheLocks: IgniteCache<String, String> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.LOCK_RECORD_CACHE)!!
        val userLocked = cacheLocks.get(key)
        return user != userLocked
    }

    fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        val cacheLocks: IgniteCache<String, String> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.LOCK_RECORD_CACHE)!!
        return if (lock) cacheLocks.putIfAbsent(key, user) else cacheLocks.remove(key, user)
    }

    fun unlockAllRecordsForUser(user: String) {
        val cacheLocks: IgniteCache<String, String> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.LOCK_RECORD_CACHE)!!
        cacheLocks.removeAll(HashSet(cacheLocks.query(ScanQuery { _, v -> v == user }, Cache.Entry<String, String>::getKey).all))
    }

    fun getUserByLogin(login: String): User {
        val cacheUser: IgniteCache<String, User> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.USER_CACHE)!!
        return cacheUser.get(login)
    }

    fun clearMenus() {
        val cacheMenu: IgniteCache<String, MenuEntry> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE)!!
        cacheMenu.clear()
    }

    fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryDto: MenuEntryDto?): MenuEntryDto {
        val cachePerson: IgniteCache<String, MenuEntry> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE)!!
        val menuEntry: MenuEntry?
        menuEntry = if (menuEntryDto.id != null) cachePerson.get(menuEntryDto.id) else MenuEntry()
        menuEntry?.name = menuEntryDto.name
        menuEntry?.url = menuEntryDto.url
        menuEntry?.roles = menuEntryDto.roles
        if (parentEntryDto != null) menuEntry?.parentId = parentEntryDto.id
        menuEntryDto.id = menuEntry?.id
        cachePerson.put(menuEntry?.id, menuEntry)
        return menuEntryDto
    }

    private fun checkIfMenuExists(menuCache: IgniteCache<String, MenuEntry>, url: String): List<Cache.Entry<String, MenuEntry>> {
        val entries: MutableList<Cache.Entry<String, MenuEntry>> = menuCache.query(SqlQuery<String, MenuEntry>(MenuEntry::class.java, "url = ?").setArgs(url)).all!!
        if (entries.isEmpty()) throw IllegalArgumentException("Menu with url: $url doesn't exist")
        return entries
    }

    fun readNextLevel(url: String, authorities: Collection<GrantedAuthority>): List<MenuEntryDto> {
        val cache: IgniteCache<String, MenuEntry> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE)!!
        val entries: List<Cache.Entry<String, MenuEntry>> = checkIfMenuExists(cache, url)
        val menuEntry: MenuEntry = entries[0].value
        val sql: SqlQuery<String, MenuEntry> = SqlQuery(MenuEntry::class.java, "parentId = ?")
        val menuEntryDtos = ArrayList<MenuEntryDto>()
        val cursor: QueryCursor<Cache.Entry<String, MenuEntry>> = cache.query(sql.setArgs(menuEntry.id))
        for (e in cursor) {
            for (authority in authorities) {
                if (e.value.roles != null && e.value.roles!!.contains(authority.authority.replace("ROLE_", ""))) {
                    menuEntryDtos.add(MenuEntryDto(e.value))
                    break
                }
            }
        }
        cursor.close()
        return menuEntryDtos
    }

    fun readBreadcrumbs(url: String): List<Breadcrumb> {
        val cache: IgniteCache<String, MenuEntry> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.MENU_CACHE)!!
        val entries: List<Cache.Entry<String, MenuEntry>> = checkIfMenuExists(cache, url)
        val original: MenuEntry = entries[0].value
        var menuEntry: MenuEntry = original
        var menuEntries: List<Cache.Entry<String, MenuEntry>>
        val breadcrumbs = ArrayList<Breadcrumb>()
        if (menuEntry.parentId == null) return breadcrumbs
        val sql = SqlQuery<String, MenuEntry>(MenuEntry::class.java, "id = ?")
        while (true) {
            menuEntries = cache.query(sql.setArgs(menuEntry.parentId)).all
            if (menuEntries.isNotEmpty()) {
                menuEntry = menuEntries[0].value
                breadcrumbs.add(0, Breadcrumb(menuEntry.name, menuEntry.url))
            } else break
        }
        breadcrumbs.add(Breadcrumb(original.name, original.url))
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

    fun selectCachePage(page: Int, pageSize: Int, sortName: String, sortOrder: String, filterDto: List<FilterDto>, cacheName: String): List<Any> {
        val cache: IgniteCache<String, Any> = ignite?.getOrCreateCache(cacheName)!!
        val cacheDtoArrayList = ArrayList<Any>()
        val sql = SqlQuery<String, Any>(UniversalFieldsDescriptor.getCacheClass(cacheName), getQuerySql(filterDto)
                .append(" order by ")
                .append(sortName).append(" ")
                .append(sortOrder)
                .append(" limit ? offset ?")
                .toString())
        val cursor = cache.query(sql.setArgs(pageSize, (page - 1) * pageSize))
        val dtoConstructor = UniversalFieldsDescriptor.getDtoClass(cacheName)?.getConstructor(UniversalFieldsDescriptor.getCacheClass(cacheName))
        for (e in cursor)
            cacheDtoArrayList.add(dtoConstructor!!.newInstance(e.value))
        cursor.close()
        return cacheDtoArrayList
    }

    fun getContactsByPersonId(id: String): List<ContactDto> {
        val cache: IgniteCache<String, Contact> = ignite?.getOrCreateCache(UniversalFieldsDescriptor.CONTACT_CACHE)!!
        val cacheDtoArrayList = ArrayList<ContactDto>()
        val sql = SqlQuery<String, Contact>(Contact::class.java, "personId = ? order by type")
        val cursor: QueryCursor<Cache.Entry<String, Contact>> = cache.query(sql.setArgs(id))
        for (e in cursor) cacheDtoArrayList.add(ContactDto(e.value))
        cursor.close()
        return cacheDtoArrayList
    }

    private fun getComparator(filterDto: FilterDto): String {
        return if (filterDto.comparator == null || filterDto.comparator.equals(""))
            " = "
        else " " + filterDto.comparator + " "
    }

    fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        if (filterDto.isEmpty()) {
            val cache: IgniteCache<Any, Any> = ignite?.getOrCreateCache(cacheName)!!
            return cache.size(CachePeekMode.ALL)
        }
        val cache: IgniteCache<String, Any> = ignite?.getOrCreateCache(cacheName)!!
        val sql = SqlQuery<String, Any>(UniversalFieldsDescriptor.getCacheClass(cacheName), getQuerySql(filterDto).toString())
        val cursor = cache.query(sql)
        val size = cursor.all.size
        cursor.close()
        return size
    }

    fun stopClient() {
        if (ignite != null) {
            ignite?.close()
        }
    }

    fun getCacheMetrics(): Map<String, CacheMetrics> {
        val cacheMetricsMap = HashMap<String, CacheMetrics>()
        for (cacheName in UniversalFieldsDescriptor.getCacheClasses().keys) {
            val cache: IgniteCache<String, Any> = ignite?.getOrCreateCache(cacheName)!!
            cacheMetricsMap[cacheName] = cache.metrics()
        }
        return cacheMetricsMap
    }
}
