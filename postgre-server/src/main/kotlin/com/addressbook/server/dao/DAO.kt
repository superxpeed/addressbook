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
        requireNotNull(organizationDto.id)
        requireNotNull(organizationDto.type)
        val organization = entityManager.find(Organization::class.java, organizationDto.id)
                ?: Organization(organizationDto)
        with(organization) {
            type = OrganizationType.values()[Integer.parseInt(organizationDto.type)]
            lastUpdated = Timestamp(System.currentTimeMillis())
            name = organizationDto.name
            addr = Address(organizationDto.street, organizationDto.zip)
        }
        entityManager.persist(organization)
        organizationDto.lastUpdated = dateFormatter.format(organization.lastUpdated)
        return organizationDto
    }

    @Transactional
    override fun getOrganizationById(id: String): OrganizationDto? {
        val organization = entityManager.find(Organization::class.java, id) ?: return null
        return OrganizationDto(organization)
    }

    @Transactional
    override fun createOrUpdatePerson(personDto: PersonDto): PersonDto {
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
    override fun getPersonById(id: String): PersonDto? {
        val person = entityManager.find(Person::class.java, id) ?: return null
        return PersonDto(person)
    }

    @Transactional
    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, targetPersonId: String): List<ContactDto> {
        val toDelete = getContactsByPersonId(targetPersonId).mapNotNull { it.id }.minus(contactDtos.mapNotNull { it.id }.toSet())
        contactDtos.forEach {
            it.personId = targetPersonId
            val contact = if (it.id == null) Contact() else entityManager.find(Contact::class.java, it.id) ?: Contact()
            with(contact) {
                data = it.data
                description = it.description
                personId = it.personId
                type = ContactType.values()[Integer.parseInt(it.type)]
            }
            entityManager.persist(contact)
            it.id = contact.contactId
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
    override fun ifOrganizationExists(key: String?): Boolean {
        return if (key == null)
            false
        else entityManager.find(Organization::class.java, key) != null
    }

    @Transactional
    override fun ifPersonExists(key: String?): Boolean {
        return if (key == null)
            false
        else entityManager.find(Person::class.java, key) != null
    }

    @Transactional
    override fun ifContactExists(key: String?): Boolean {
        return if (key == null)
            false
        else entityManager.find(Contact::class.java, key) != null
    }

    override fun ifPageExists(page: String): Boolean {
        return getMenuEntriesByUrl(page).isNotEmpty()
    }

    @Transactional
    override fun saveDocument(document: DocumentDto) {
        val targetDocument = Document()
        targetDocument.id = document.id
        targetDocument.name = document.name
        targetDocument.checksum = document.checksum
        targetDocument.personId = document.personId
        targetDocument.size = document.size
        entityManager.persist(targetDocument)
    }

    @Transactional
    override fun deleteDocument(id: String) {
        entityManager.remove(entityManager.find(Document::class.java, id))
    }

    override fun getDocumentsByPersonId(id: String): List<DocumentDto> {
        return entityManager.createQuery("SELECT u FROM Document u WHERE u.personId=:id ORDER BY u.createDate ASC ", Document::class.java)
                .also { it?.setParameter("id", id) }
                ?.resultList
                ?.map { DocumentDto(it) }
                ?.toList()
                ?: emptyList()
    }

    override fun getDocumentById(id: String): DocumentDto? {
        val document = entityManager.find(Document::class.java, id) ?: return null
        return DocumentDto(document)
    }

    @Transactional
    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        if (lock) {
            val userLocked = entityManager.find(Lock::class.java, key)
            return if (userLocked == null) {
                entityManager.persist(Lock(key, user))
                true
            } else return user.lowercase() == userLocked.login?.lowercase()
        } else {
            val userLocked = entityManager.find(Lock::class.java, key) ?: return true
            return if (user.lowercase() == userLocked.login?.lowercase()) {
                entityManager.remove(userLocked)
                true
            } else
                false
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

    fun getMenuEntriesByUrl(url: String): List<MenuEntry> {
        return entityManager.createQuery("SELECT u FROM MenuEntry u WHERE u.url=:url", MenuEntry::class.java)
                .also { it?.setParameter("url", url) }?.resultList ?: emptyList()
    }

    @Transactional
    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        val entries = getMenuEntriesByUrl(url)
        if (entries.isEmpty()) return emptyList()
        val menuEntries = ArrayList<MenuEntryDto>()
        entityManager.createQuery("SELECT u FROM MenuEntry u WHERE u.parentId=:parentId", MenuEntry::class.java)
                .also { it?.setParameter("parentId", entries[0].id) }
                ?.resultList?.forEach { e ->
                    for (authority in authorities) {
                        if (e.roles != null && e.roles!!.contains(authority.replace("ROLE_", ""))) {
                            menuEntries.add(MenuEntryDto(e))
                            break
                        }
                    }
                }
        return menuEntries
    }

    @Transactional
    override fun readBreadcrumbs(url: String): List<BreadcrumbDto> {
        val entries = getMenuEntriesByUrl(url)
        if (entries.isEmpty()) return emptyList()
        val original = entries[0]
        var menuEntry = original
        if (menuEntry.parentId == null) {
            return listOf(BreadcrumbDto(original.name, original.url))
        }
        val breadcrumbs = ArrayList<BreadcrumbDto>()
        while (true) {
            val menuEntries = entityManager.createQuery("SELECT u FROM MenuEntry u WHERE u.id=:id", MenuEntry::class.java)
                    .also { it?.setParameter("id", menuEntry.parentId) }?.resultList as List<MenuEntry>
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
                    "TextFilter" -> {
                        addSql = if (it.name == "type") {
                            it.value?.let { typeOrdinal ->
                                if (typeOrdinal.isNotBlank() && typeOrdinal.toInt() >= 0 && typeOrdinal.toInt() < OrganizationType.values().size)
                                    it.name + " LIKE '%" + OrganizationType.values()[typeOrdinal.toInt()].name + "%'"
                                else ""
                            } ?: ""
                        } else {
                            it.name + " LIKE '%" + it.value?.replace("'", "''") + "%'"
                        }
                    }
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
                                addSql = it.name + " < TO_TIMESTAMP('" + it.value + tailLower + " , " + format + ") OR " + it.name + " > TO_TIMESTAMP('" + it.value + tailUpper + " , " + format + ")"
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
                if (filterDto.indexOf(it) == 0) baseSql.append(" WHERE ")
                baseSql.append(addSql)
                if (filterDto.indexOf(it) != (filterDto.size - 1)) baseSql.append(" AND ")
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
        return entityManager.createQuery("SELECT u FROM Contact u WHERE u.personId=:id ORDER BY u.createDate ASC ", Contact::class.java)
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
