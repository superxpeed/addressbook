package com.addressbook.server.dao

import com.addressbook.AddressBookDAO
import com.addressbook.FieldDescriptor
import com.addressbook.dto.*
import com.addressbook.model.*
import org.springframework.stereotype.Controller
import java.sql.Timestamp
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional


@Controller
class DAO : AddressBookDAO {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    override fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        val organization = entityManager.find(Organization::class.java, organizationDto.id)
                ?: Organization(organizationDto)
        with(organization) {
            type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
            lastUpdated = Timestamp(System.currentTimeMillis())
            name = organizationDto.name
            addr = Address(organizationDto.street, organizationDto.zip)
        }
        entityManager.persist(organization)
        return organizationDto
    }

    @Transactional
    override fun createOrUpdatePerson(personDto: PersonDto, user: String): PersonDto {
        val person = personDto.id?.let { entityManager.find(Person::class.java, personDto.id) }
                ?: Person().also { personDto.id = it.id }
        with(person) {
            firstName = personDto.firstName
            lastName = personDto.lastName
            orgId = personDto.orgId
            salary = personDto.salary
            resume = personDto.resume
        }
        entityManager.persist(person)
        return personDto
    }

    @Transactional
    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, targetPersonId: String): List<ContactDto> {
        if (contactDtos.isEmpty()) return contactDtos
        val toDelete = getContactsByPersonId(targetPersonId).mapNotNull { it.id }.minus(contactDtos.mapNotNull { it.id }.toSet())
        contactDtos.forEach {
            it.personId = targetPersonId
            val contact = entityManager.find(Contact::class.java, it.id) ?: Contact()
            with(contact) {
                data = it.data
                description = it.description
                personId = it.personId
                type = ContactType.values()[Integer.parseInt(it.type)]
            }
            entityManager.persist(contact)
        }
        toDelete.forEach { entityManager.remove(entityManager.find(Contact::class.java, it)) }
        return contactDtos
    }

    @Transactional
    override fun createOrUpdateUser(newUser: User) {
        var user = entityManager.find(User::class.java, newUser.login)
        user?.let {
            it.password = newUser.password
            it.roles = newUser.roles
        } ?: let { user = newUser }
        entityManager.persist(user)
    }

    @Transactional
    override fun notLockedByUser(key: String, user: String): Boolean {
        val userLocked = entityManager.find(Lock::class.java, key) ?: return false
        return user != userLocked.login
    }

    @Transactional
    override fun ifOrganizationExists(key: String): Boolean {
        return entityManager.find(Organization::class.java, key) != null
    }

    @Transactional
    override fun ifPersonExists(key: String): Boolean {
        return entityManager.find(Person::class.java, key) != null
    }

    @Transactional
    override fun ifContactExists(key: String): Boolean {
        return entityManager.find(Contact::class.java, key) != null
    }

    override fun ifPageExists(page: String): Boolean {
        try {
            checkIfMenuExists(page)
        } catch (e: IllegalArgumentException) {
            return false
        }
        return true
    }

    @Transactional
    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        if (lock) {
            val userLocked = entityManager.find(Lock::class.java, key)
            return if(userLocked == null) {
                entityManager.persist(Lock(key, user))
                true
            } else true
        } else {
            val userLocked = entityManager.find(Lock::class.java, key) ?: return false
            entityManager.remove(userLocked)
            return true
        }
    }

    @Transactional
    override fun unlockAllRecordsForUser(user: String) {
        entityManager.createQuery("SELECT u FROM Lock u WHERE u.login=:user", Lock::class.java)
                .also { it?.setParameter("user", user) }
                ?.resultList
                ?.forEach { u -> entityManager.remove(u) }
    }

    @Transactional
    override fun getUserByLogin(login: String): User? {
        return entityManager.find(User::class.java, login)
    }

    @Transactional
    override fun clearMenus() {
        entityManager.createNativeQuery("DELETE FROM menu_entry_roles")?.executeUpdate()
        entityManager.createNativeQuery("DELETE FROM menus")?.executeUpdate()
    }

    @Transactional
    override fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryId: String?): MenuEntryDto {
        val menuEntry = menuEntryDto.id
                ?.let { entityManager.find(MenuEntry::class.java, menuEntryDto.id) }
                ?: MenuEntry()
        with(menuEntry) {
            name = menuEntryDto.name
            url = menuEntryDto.url
            roles = menuEntryDto.roles
            parentEntryId?.let { menuEntry.parentId = parentEntryId }
        }
        menuEntryDto.id = menuEntry.id
        entityManager.persist(menuEntry)
        return menuEntryDto
    }

    fun checkIfMenuExists(url: String): List<MenuEntry> {
        val results = entityManager.createQuery("SELECT u FROM MenuEntry u WHERE u.url=:url", MenuEntry::class.java)
                .also { it?.setParameter("url", url) }?.resultList
        return if (!results.isNullOrEmpty()) results else throw IllegalArgumentException("Menu with url: $url doesn't exist")
    }

    @Transactional
    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        val menuEntryDtos = ArrayList<MenuEntryDto>()
        entityManager.createQuery("SELECT u FROM MenuEntry u WHERE u.parentId=:parentId", MenuEntry::class.java)
                .also { it?.setParameter("parentId", checkIfMenuExists(url)[0].id) }
                ?.resultList?.forEach { e ->
                    for (authority in authorities) {
                        if (e.roles != null && e.roles!!.contains(authority.replace("ROLE_", ""))) {
                            menuEntryDtos.add(MenuEntryDto(e))
                            break
                        }
                    }
                }
        return menuEntryDtos
    }

    @Transactional
    override fun readBreadcrumbs(url: String): List<BreadcrumbDto> {
        val original = checkIfMenuExists(url)[0]
        var menuEntry = original
        val breadcrumbs = ArrayList<BreadcrumbDto>()
        if (menuEntry.parentId == null) return breadcrumbs
        while (true) {
            val menuEntries = entityManager.createQuery("SELECT u FROM MenuEntry u WHERE u.id=:id", MenuEntry::class.java)
                    .also { it?.setParameter("id", menuEntry.parentId) }?.resultList as MutableList<MenuEntry>
            if (menuEntries.isNotEmpty()) {
                menuEntry = menuEntries[0]
                breadcrumbs.add(0, BreadcrumbDto(menuEntry.name, menuEntry.url))
            } else break
        }
        breadcrumbs.add(BreadcrumbDto(original.name, original.url))
        return breadcrumbs
    }

    private fun getQuerySql(filterDto: List<FilterDto>): StringBuilder {
        val baseSql = StringBuilder(" ")
        if (filterDto.isNotEmpty()) {
            filterDto.forEach {
                var addSql = ""
                when (it.type) {
                    "NumberFilter" -> {
                        addSql = it.name + getComparator(it) + Integer.parseInt(it.value)
                    }
                    "TextFilter" -> addSql = it.name + " like '%" + it.value?.replace("'", "''") + "%'"
                    "DateFilter" -> {
                        it.value = it.value?.substring(0, 10)
                        val tailLower = " 00:00:00.00000'"
                        val tailUpper = " 23:59:59.999999'"
                        val format = "'YYYY-MM-DD HH24:MI:SS.US'"
                        when (it.comparator) {
                            "=" -> {
                                addSql = it.name + " BETWEEN TO_TIMESTAMP('" + it.value + tailLower + " , " + format + ") AND TO_TIMESTAMP('" + it.value + tailUpper + " , " + format + ")"
                            }
                            "!=" -> {
                                addSql = it.name + " < TO_TIMESTAMP('" + it.value + tailLower + " , " + format + ") AND " + it.name + " > TO_TIMESTAMP('" + it.value + tailUpper + " , " + format + ")"
                            }
                            ">" -> {
                                addSql = it.name + " > TO_TIMESTAMP('" + it.value + tailUpper + " , " + format + ")"
                            }
                            ">=" -> {
                                addSql = it.name + " > TO_TIMESTAMP('" + it.value + tailLower + " , " + format + ")"
                            }
                            "<=" -> {
                                addSql = it.name + " < TO_TIMESTAMP('" + it.value + tailUpper + " , " + format + ")"
                            }
                            "<" -> {
                                addSql = it.name + " < TO_TIMESTAMP('" + it.value + tailLower + " , " + format + ")"
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

    private fun getComparator(filterDto: FilterDto): String {
        return if (filterDto.comparator == null || filterDto.comparator.equals("")) " = "
        else " " + filterDto.comparator + " "
    }

    override fun selectCachePage(page: Int, pageSize: Int, sortName: String, sortOrder: String, filterDto: List<FilterDto>, cacheName: String): List<Any> {
        val cacheDtoArrayList = ArrayList<Any>()
        val dtoConstructor = FieldDescriptor.getDtoClass(cacheName)?.getConstructor(FieldDescriptor.getCacheClass(cacheName))
        entityManager.createQuery("SELECT u FROM "
                + (FieldDescriptor.getCacheClass(cacheName)?.simpleName
                + " u "
                + getQuerySql(filterDto)
                + " order by "
                + sortName
                + " "
                + sortOrder
                + " "),
                FieldDescriptor.getCacheClass(cacheName))
                .also { it?.maxResults = pageSize }
                .also { it?.firstResult = (page - 1) * pageSize }
                ?.resultList
                ?.forEach { e -> dtoConstructor?.newInstance(e)?.let { cacheDtoArrayList.add(it) } }
        return cacheDtoArrayList
    }

    override fun getContactsByPersonId(id: String): List<ContactDto> {
        return entityManager.createQuery("SELECT u FROM Contact u WHERE u.personId=:id order by u.createDate asc ", Contact::class.java)
                .also { it?.setParameter("id", id) }
                ?.resultList
                ?.map { ContactDto(it) }
                ?.toList()
                ?: emptyList()
    }

    override fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        return entityManager.createQuery("SELECT count(u) FROM "
                + (FieldDescriptor.getCacheClass(cacheName)?.simpleName
                + " u "
                + getQuerySql(filterDto)),
                Long::class.javaObjectType)
                ?.singleResult
                ?.toInt()
                ?: 0
    }
}
