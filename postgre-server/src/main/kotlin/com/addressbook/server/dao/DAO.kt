package com.addressbook.server.dao

import com.addressbook.AddressBookDAO
import com.addressbook.FieldDescriptor
import com.addressbook.dto.*
import com.addressbook.model.*
import org.springframework.stereotype.Controller
import java.sql.Timestamp
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery
import javax.transaction.Transactional


@Controller
class DAO : AddressBookDAO {

    @PersistenceContext
    private val entityManager: EntityManager? = null

    @Transactional
    override fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        var organization = entityManager?.find(Organization::class.java, organizationDto.id)
        if (organization == null) {
            organization = Organization(organizationDto)
        }
        organization.type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
        organization.lastUpdated = Timestamp(System.currentTimeMillis())
        organization.name = organizationDto.name
        organization.addr = Address(organizationDto.street, organizationDto.zip)
        entityManager?.persist(organization)
        return organizationDto
    }

    @Transactional
    override fun createOrUpdatePerson(personDto: PersonDto, user: String): PersonDto {
        var person = if (personDto.id != null) entityManager?.find(Person::class.java, personDto.id) else null
        if (person == null) {
            person = Person()
            personDto.id = person.id
        }
        person.firstName = personDto.firstName
        person.lastName = personDto.lastName
        person.orgId = personDto.orgId
        person.salary = personDto.salary
        person.resume = personDto.resume
        entityManager?.persist(person)
        return personDto
    }

    @Transactional
    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, personId: String): List<ContactDto> {
        if (contactDtos.isEmpty()) return contactDtos
        var toDelete = getContactsByPersonId(personId).mapNotNull { it.id }
        val updated = contactDtos.mapNotNull { it.id }
        toDelete = toDelete.minus(updated)
        for (contactDto in contactDtos) {
            contactDto.personId = personId
            var contact = entityManager?.find(Contact::class.java, contactDto.id)
            if (contact == null) contact = Contact()
            contact.data = contactDto.data
            contact.description = contactDto.description
            contact.personId = contactDto.personId
            contact.type = ContactType.values()[Integer.parseInt(contactDto.type)]
            entityManager?.persist(contact)
        }
        for (id in toDelete) {
            entityManager?.remove(entityManager.find(Contact::class.java, id))
        }
        return contactDtos
    }

    @Transactional
    override fun createOrUpdateUser(newUser: User): String {
        var user = entityManager?.find(User::class.java, newUser.login)
        if (user != null) {
            user.password = newUser.password
            user.roles = newUser.roles
        } else user = newUser
        entityManager?.persist(user)
        return "OK"
    }

    @Transactional
    override fun notLockedByUser(key: String, user: String): Boolean {
        val userLocked = entityManager?.find(Lock::class.java, key) ?: return false
        return user != userLocked.login
    }

    @Transactional
    override fun ifOrganizationExists(key: String): Boolean {
        return entityManager?.find(Organization::class.java, key) != null
    }

    @Transactional
    override fun ifPersonExists(key: String): Boolean {
        return entityManager?.find(Person::class.java, key) != null
    }

    @Transactional
    override fun ifContactExists(key: String): Boolean {
        return entityManager?.find(Contact::class.java, key) != null
    }

    override fun ifPageExists(page: String): Boolean {
        try {
            checkIfMenuExists(page)
        }catch (e : IllegalArgumentException){
            return false
        }
        return true
    }

    @Transactional
    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        if (lock) {
            entityManager?.persist(Lock(key, user))
        } else {
            val userLocked = entityManager?.find(Lock::class.java, key) ?: return false
            entityManager.remove(userLocked)
        }
        return true
    }

    @Transactional
    override fun unlockAllRecordsForUser(user: String): String {
        val typedQuery: TypedQuery<Lock>? = entityManager?.createQuery("SELECT u FROM Lock u WHERE u.login=:user", Lock::class.java)
        typedQuery?.setParameter("user", user)
        val results = typedQuery?.resultList
        if (results != null) {
            for (u in results) {
                entityManager?.remove(u)
            }
        }
        return "OK"
    }

    @Transactional
    override fun getUserByLogin(login: String): User? {
        return entityManager?.find(User::class.java, login)
    }

    @Transactional
    override fun clearMenus(): String {
        entityManager?.createNativeQuery("DELETE FROM menu_entry_roles")?.executeUpdate()
        entityManager?.createNativeQuery("DELETE FROM menus")?.executeUpdate()
        return "OK"
    }

    @Transactional
    override fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryId: String?): MenuEntryDto {
        val menuEntry = if (menuEntryDto.id != null) entityManager?.find(MenuEntry::class.java, menuEntryDto.id) else MenuEntry()
        menuEntry?.name = menuEntryDto.name
        menuEntry?.url = menuEntryDto.url
        menuEntry?.roles = menuEntryDto.roles
        if (parentEntryId != null) menuEntry?.parentId = parentEntryId
        menuEntryDto.id = menuEntry?.id
        entityManager?.persist(menuEntry)
        return menuEntryDto
    }

    fun checkIfMenuExists(url: String): List<MenuEntry>? {
        val typedQuery = entityManager?.createQuery("SELECT u FROM MenuEntry u WHERE u.url=:url", MenuEntry::class.java)
        typedQuery?.setParameter("url", url)
        val results = typedQuery?.resultList
        if (results != null && results.isEmpty()) throw IllegalArgumentException("Menu with url: $url doesn't exist")
        return results
    }

    @Transactional
    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        val entries = checkIfMenuExists(url)
        val menuEntry = entries?.get(0)
        val menuEntryDtos = ArrayList<MenuEntryDto>()
        val typedQuery = entityManager?.createQuery("SELECT u FROM MenuEntry u WHERE u.parentId=:parentId", MenuEntry::class.java)
        typedQuery?.setParameter("parentId", menuEntry?.id)
        val cursor = typedQuery?.resultList
        if (cursor != null) {
            for (e in cursor) {
                for (authority in authorities) {
                    if (e.roles != null && e.roles!!.contains(authority.replace("ROLE_", ""))) {
                        menuEntryDtos.add(MenuEntryDto(e))
                        break
                    }
                }
            }
        }
        return menuEntryDtos
    }

    @Transactional
    override fun readBreadcrumbs(url: String): List<BreadcrumbDto> {
        val entries = checkIfMenuExists(url)
        val original = entries?.get(0)
        var menuEntry = original
        var menuEntries: MutableList<MenuEntry>?
        val breadcrumbs = ArrayList<BreadcrumbDto>()
        if (menuEntry?.parentId == null) return breadcrumbs
        while (true) {
            val typedQuery = entityManager?.createQuery("SELECT u FROM MenuEntry u WHERE u.id=:id", MenuEntry::class.java)
            typedQuery?.setParameter("id", menuEntry?.parentId)
            menuEntries = typedQuery?.resultList as MutableList<MenuEntry>
            if (menuEntries.isNotEmpty()) {
                menuEntry = menuEntries[0]
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
                if (type.equals("DateFilter")) {
                    filter.value = filter.value?.substring(0, 10)
                    val tailLower = " 00:00:00.00000'"
                    val tailUpper = " 23:59:59.999999'"
                    val format = "'YYYY-MM-DD HH24:MI:SS.US'"
                    when (filter.comparator) {
                        "=" -> {
                            addSql = filter.name + " BETWEEN TO_TIMESTAMP('" + filter.value + tailLower + " , " + format + ") AND TO_TIMESTAMP('" + filter.value + tailUpper + " , " + format + ")"
                        }
                        "!=" -> {
                            addSql = filter.name + " < TO_TIMESTAMP('" + filter.value + tailLower + " , " + format + ") AND " + filter.name + " > TO_TIMESTAMP('" + filter.value + tailUpper + " , " + format + ")"
                        }
                        ">" -> {
                            addSql = filter.name + " > TO_TIMESTAMP('" + filter.value + tailUpper + " , " + format + ")"
                        }
                        ">=" -> {
                            addSql = filter.name + " > TO_TIMESTAMP('" + filter.value + tailLower + " , " + format + ")"
                        }
                        "<=" -> {
                            addSql = filter.name + " < TO_TIMESTAMP('" + filter.value + tailUpper + " , " + format + ")"
                        }
                        "<" -> {
                            addSql = filter.name + " < TO_TIMESTAMP('" + filter.value + tailLower + " , " + format + ")"
                        }
                    }
                }
                if (filterDto.indexOf(filter) == 0) baseSql.append(" where ")
                baseSql.append(addSql)
                if (filterDto.indexOf(filter) != (filterDto.size - 1)) baseSql.append(" and ")
            }
        }
        return baseSql
    }

    private fun getComparator(filterDto: FilterDto): String {
        return if (filterDto.comparator == null || filterDto.comparator.equals(""))
            " = "
        else " " + filterDto.comparator + " "
    }

    override fun selectCachePage(page: Int, pageSize: Int, sortName: String, sortOrder: String, filterDto: List<FilterDto>, cacheName: String): List<Any> {
        val cacheDtoArrayList = ArrayList<Any>()
        val typedQuery = entityManager?.createQuery("SELECT u FROM " + (FieldDescriptor.getCacheClass(cacheName)?.simpleName
                + " u " + getQuerySql(filterDto) + " order by " + sortName + " " + sortOrder + " "), FieldDescriptor.getCacheClass(cacheName))
        typedQuery?.maxResults = pageSize
        typedQuery?.firstResult = (page - 1) * pageSize
        val cursor = typedQuery?.resultList
        val dtoConstructor = FieldDescriptor.getDtoClass(cacheName)?.getConstructor(FieldDescriptor.getCacheClass(cacheName))
        if (cursor != null) {
            for (e in cursor)
                dtoConstructor?.newInstance(e)?.let { cacheDtoArrayList.add(it) }
        }
        return cacheDtoArrayList
    }

    override fun getContactsByPersonId(id: String): List<ContactDto> {
        val cacheDtoArrayList = ArrayList<ContactDto>()
        val typedQuery = entityManager?.createQuery("SELECT u FROM Contact u WHERE u.personId=:id order by u.type asc ", Contact::class.java)
        typedQuery?.setParameter("id", id)
        val contacts = typedQuery?.resultList as MutableList<Contact>
        for (e in contacts) cacheDtoArrayList.add(ContactDto(e))
        return cacheDtoArrayList
    }

    override fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        val typedQuery = entityManager?.createQuery("SELECT count(u) FROM " + (FieldDescriptor.getCacheClass(cacheName)?.simpleName + " u " + getQuerySql(filterDto)), Long::class.javaObjectType)
        return if (typedQuery != null && typedQuery.singleResult != null) typedQuery.singleResult.toInt() else return 0
    }
}
