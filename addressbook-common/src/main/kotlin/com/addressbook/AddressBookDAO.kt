package com.addressbook

import com.addressbook.annotations.LoggedGetRequest
import com.addressbook.annotations.LoggedPostRequest
import com.addressbook.dto.*
import com.addressbook.model.User
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@ResponseBody
interface AddressBookDAO {

    @LoggedPostRequest("/createOrUpdateOrganization")
    fun createOrUpdateOrganization(@RequestBody organizationDto: OrganizationDto, @RequestParam user: String): OrganizationDto

    @LoggedGetRequest("/getOrganizationById")
    fun getOrganizationById(@RequestParam id: String): OrganizationDto?

    @LoggedPostRequest("/createOrUpdatePerson")
    fun createOrUpdatePerson(@RequestBody personDto: PersonDto): PersonDto

    @LoggedGetRequest("/getPersonById")
    fun getPersonById(@RequestParam id: String): PersonDto?

    @LoggedPostRequest("/createOrUpdateContacts")
    fun createOrUpdateContacts(@RequestBody contactDtos: List<ContactDto>, @RequestParam user: String, @RequestParam targetPersonId: String): List<ContactDto>

    @LoggedPostRequest("/createOrUpdateUser")
    fun createOrUpdateUser(@RequestBody newUser: User)

    @LoggedPostRequest("/lockUnlockRecord")
    fun lockUnlockRecord(@RequestParam key: String, @RequestParam user: String, @RequestParam lock: Boolean): Boolean

    @LoggedPostRequest("/unlockAllRecordsForUser")
    fun unlockAllRecordsForUser(@RequestParam user: String)

    @LoggedPostRequest("/getUserByLogin")
    fun getUserByLogin(@RequestParam login: String): User?

    @LoggedPostRequest("/clearMenus")
    fun clearMenus()

    @LoggedPostRequest("/createOrUpdateMenuEntry")
    fun createOrUpdateMenuEntry(@RequestBody menuEntryDto: MenuEntryDto, @RequestParam parentEntryId: String?): MenuEntryDto

    @LoggedPostRequest("/readNextLevel")
    fun readNextLevel(@RequestParam url: String, @RequestBody authorities: List<String>): List<MenuEntryDto>

    @LoggedPostRequest("/readBreadcrumbs")
    fun readBreadcrumbs(@RequestParam url: String): List<BreadcrumbDto>

    @LoggedPostRequest("/selectCachePage")
    fun selectCachePage(@RequestParam page: Int, @RequestParam pageSize: Int, @RequestParam sortName: String, @RequestParam sortOrder: String, @RequestBody filterDto: List<FilterDto>, @RequestParam cacheName: String): List<Any>

    @LoggedPostRequest("/getContactsByPersonId")
    fun getContactsByPersonId(@RequestParam id: String): List<ContactDto>

    @LoggedPostRequest("/getTotalDataSize")
    fun getTotalDataSize(@RequestParam cacheName: String, @RequestBody filterDto: List<FilterDto>): Int

    @LoggedPostRequest("/notLockedByUser")
    fun notLockedByUser(@RequestParam key: String, @RequestParam user: String): Boolean

    @LoggedPostRequest("/ifOrganizationExists")
    fun ifOrganizationExists(@RequestParam key: String?): Boolean

    @LoggedPostRequest("/ifPersonExists")
    fun ifPersonExists(@RequestParam key: String?): Boolean

    @LoggedPostRequest("/ifContactExists")
    fun ifContactExists(@RequestParam key: String?): Boolean

    @LoggedPostRequest("/ifPageExists")
    fun ifPageExists(@RequestParam page: String): Boolean

    @LoggedPostRequest("/saveDocument")
    fun saveDocument(@RequestBody document: DocumentDto)

    @LoggedGetRequest("/deleteDocument")
    fun deleteDocument(@RequestParam id: String)

    @LoggedGetRequest("/getDocumentsByPersonId")
    fun getDocumentsByPersonId(@RequestParam id: String): List<DocumentDto>

    @LoggedGetRequest("/getDocumentById")
    fun getDocumentById(@RequestParam id: String): DocumentDto?
}
