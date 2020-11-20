package com.addressbook

import com.addressbook.dto.*
import com.addressbook.model.User
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@ResponseBody
interface AddressBookDAO {

    @PostMapping("/createOrUpdateOrganization")
    fun createOrUpdateOrganization(@RequestBody organizationDto: OrganizationDto, @RequestParam user: String): OrganizationDto

    @PostMapping("/createOrUpdatePerson")
    fun createOrUpdatePerson(@RequestBody personDto: PersonDto, @RequestParam user: String): PersonDto

    @PostMapping("/createOrUpdateContacts")
    fun createOrUpdateContacts(@RequestBody contactDtos: List<ContactDto>, @RequestParam user: String, @RequestParam personId: String): List<ContactDto>

    @PostMapping("/createOrUpdateUser")
    fun createOrUpdateUser(@RequestBody newUser: User): String

    @PostMapping("/lockUnlockRecord")
    fun lockUnlockRecord(@RequestParam key: String, @RequestParam user: String, @RequestParam lock: Boolean): Boolean

    @PostMapping("/unlockAllRecordsForUser")
    fun unlockAllRecordsForUser(@RequestParam user: String): String

    @PostMapping("/getUserByLogin")
    fun getUserByLogin(@RequestParam login: String): User?

    @PostMapping("/clearMenus")
    fun clearMenus(): String

    @PostMapping("/createOrUpdateMenuEntry")
    fun createOrUpdateMenuEntry(@RequestBody menuEntryDto: MenuEntryDto, @RequestParam parentEntryId: String?): MenuEntryDto

    @PostMapping("/readNextLevel")
    fun readNextLevel(@RequestParam url: String, @RequestBody authorities: List<String>): List<MenuEntryDto>

    @PostMapping("/readBreadcrumbs")
    fun readBreadcrumbs(@RequestParam url: String): List<Breadcrumb>

    @PostMapping("/selectCachePage")
    fun selectCachePage(@RequestParam page: Int, @RequestParam pageSize: Int, @RequestParam sortName: String, @RequestParam sortOrder: String, @RequestBody filterDto: List<FilterDto>, @RequestParam cacheName: String): List<Any>

    @PostMapping("/getContactsByPersonId")
    fun getContactsByPersonId(@RequestParam id: String): List<ContactDto>

    @PostMapping("/getTotalDataSize")
    fun getTotalDataSize(@RequestParam cacheName: String, @RequestBody filterDto: List<FilterDto>): Int

    @PostMapping("/notLockedByUser")
    fun notLockedByUser(@RequestParam key: String, @RequestParam user: String): Boolean

    @PostMapping("/ifOrganizationExists")
    fun ifOrganizationExists(@RequestParam key: String): Boolean

    @PostMapping("/ifPersonExists")
    fun ifPersonExists(@RequestParam key: String): Boolean

    @PostMapping("/ifContactExists")
    fun ifContactExists(@RequestParam key: String): Boolean
}
