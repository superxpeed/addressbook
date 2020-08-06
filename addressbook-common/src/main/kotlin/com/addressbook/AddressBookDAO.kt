package com.addressbook

import com.addressbook.dto.*
import com.addressbook.model.User
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

interface AddressBookDAO {

    @ResponseBody
    @RequestMapping("/createOrUpdateOrganization")
    fun createOrUpdateOrganization(@RequestBody organizationDto: OrganizationDto, @RequestParam user: String): OrganizationDto

    @ResponseBody
    @RequestMapping("/createOrUpdatePerson")
    fun createOrUpdatePerson(@RequestBody personDto: PersonDto, @RequestParam user: String): PersonDto

    @ResponseBody
    @RequestMapping("/createOrUpdateContacts")
    fun createOrUpdateContacts(@RequestBody contactDtos: List<ContactDto>, @RequestParam user: String, @RequestParam personId: String): List<ContactDto>

    @ResponseBody
    @RequestMapping("/createOrUpdateUser")
    fun createOrUpdateUser(@RequestBody newUser: User): String

    @ResponseBody
    @RequestMapping("/lockUnlockRecord")
    fun lockUnlockRecord(@RequestParam key: String, @RequestParam user: String, @RequestParam lock: Boolean): Boolean

    @ResponseBody
    @RequestMapping("/unlockAllRecordsForUser")
    fun unlockAllRecordsForUser(@RequestParam user: String): String

    @ResponseBody
    @RequestMapping("/getUserByLogin")
    fun getUserByLogin(@RequestParam login: String): User?

    @ResponseBody
    @RequestMapping("/clearMenus")
    fun clearMenus(): String

    @ResponseBody
    @RequestMapping("/createOrUpdateMenuEntry")
    fun createOrUpdateMenuEntry(@RequestBody menuEntryDto: MenuEntryDto, @RequestParam parentEntryId: String?): MenuEntryDto

    @ResponseBody
    @RequestMapping("/readNextLevel")
    fun readNextLevel(@RequestParam url: String, @RequestBody authorities: List<String>): List<MenuEntryDto>

    @ResponseBody
    @RequestMapping("/readBreadcrumbs")
    fun readBreadcrumbs(@RequestParam url: String): List<Breadcrumb>

    @ResponseBody
    @RequestMapping("/selectCachePage")
    fun selectCachePage(@RequestParam page: Int, @RequestParam pageSize: Int, @RequestParam sortName: String, @RequestParam sortOrder: String, @RequestBody filterDto: List<FilterDto>, @RequestParam cacheName: String): List<Any>

    @ResponseBody
    @RequestMapping("/getContactsByPersonId")
    fun getContactsByPersonId(@RequestParam id: String): List<ContactDto>

    @ResponseBody
    @RequestMapping("/getTotalDataSize")
    fun getTotalDataSize(@RequestParam cacheName: String, @RequestBody filterDto: List<FilterDto>): Int

    @ResponseBody
    @RequestMapping("/notLockedByUser")
    fun notLockedByUser(@RequestParam key: String, @RequestParam user: String): Boolean

    @ResponseBody
    @RequestMapping("/ifOrganizationExists")
    fun ifOrganizationExists(@RequestParam key: String): Boolean

    @ResponseBody
    @RequestMapping("/ifPersonExists")
    fun ifPersonExists(@RequestParam key: String): Boolean

    @ResponseBody
    @RequestMapping("/ifContactExists")
    fun ifContactExists(@RequestParam key: String): Boolean
}
