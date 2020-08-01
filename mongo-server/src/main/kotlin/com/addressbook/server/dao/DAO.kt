package com.addressbook.server.dao

import com.addressbook.AddressBookDAO
import com.addressbook.dto.*
import com.addressbook.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Controller
class DAO : AddressBookDAO {

    private val logger = LoggerFactory.getLogger(DAO::class.java)

    @PostConstruct
    fun startClient() {
        TODO("Not yet implemented")
    }

    override fun createOrUpdateOrganization(organizationDto: OrganizationDto, user: String): OrganizationDto {
        TODO("Not yet implemented")
    }

    override fun createOrUpdatePerson(personDto: PersonDto, user: String): PersonDto {
        TODO("Not yet implemented")
    }

    override fun createOrUpdateContacts(contactDtos: List<ContactDto>, user: String, personId: String): List<ContactDto> {
        TODO("Not yet implemented")
    }

    override fun createOrUpdateUser(newUser: User): String {
        TODO("Not yet implemented")
    }

    override fun notLockedByUser(key: String, user: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun ifOrganizationExists(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun ifPersonExists(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun ifContactExists(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun lockUnlockRecord(key: String, user: String, lock: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun unlockAllRecordsForUser(user: String): String {
        TODO("Not yet implemented")
    }

    override fun getUserByLogin(login: String): User? {
        TODO("Not yet implemented")
    }

    override fun clearMenus(): String {
        TODO("Not yet implemented")
    }

    override fun createOrUpdateMenuEntry(menuEntryDto: MenuEntryDto, parentEntryId: String?): MenuEntryDto {
        TODO("Not yet implemented")
    }

    override fun readNextLevel(url: String, authorities: List<String>): List<MenuEntryDto> {
        TODO("Not yet implemented")
    }

    override fun readBreadcrumbs(url: String): List<Breadcrumb> {
        TODO("Not yet implemented")
    }

    override fun selectCachePage(page: Int, pageSize: Int, sortName: String, sortOrder: String, filterDto: List<FilterDto>, cacheName: String): List<Any> {
        TODO("Not yet implemented")
    }

    override fun getContactsByPersonId(id: String): List<ContactDto> {
        TODO("Not yet implemented")
    }

    override fun getTotalDataSize(cacheName: String, filterDto: List<FilterDto>): Int {
        TODO("Not yet implemented")
    }

    @PreDestroy
    fun stopClient() {
        TODO("Not yet implemented")
    }
}
