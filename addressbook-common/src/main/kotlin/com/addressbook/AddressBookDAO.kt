package com.addressbook

import com.addressbook.dto.*
import com.addressbook.model.User
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMethod

interface AddressBookDAO {

    @ResponseBody
    @RequestMapping("/createOrUpdateOrganization", method = [RequestMethod.POST])
    fun createOrUpdateOrganization(@RequestBody organizationDto: OrganizationDto, @RequestParam user: String): OrganizationDto

    @ResponseBody
    @RequestMapping("/createOrUpdatePerson", method = [RequestMethod.POST])
    fun createOrUpdatePerson(@RequestBody personDto: PersonDto, @RequestParam user: String): PersonDto

    @ResponseBody
    @RequestMapping("/createOrUpdateContacts", method = [RequestMethod.POST])
    fun createOrUpdateContacts(@RequestBody contactDtos: List<ContactDto>, @RequestParam user: String, @RequestParam personId: String): List<ContactDto>

    @ResponseBody
    @RequestMapping("/createOrUpdateUser", method = [RequestMethod.POST])
    fun createOrUpdateUser(@RequestBody newUser: User): String

    @ResponseBody
    @RequestMapping("/lockUnlockRecord", method = [RequestMethod.POST])
    fun lockUnlockRecord(@RequestParam key: String, @RequestParam user: String, @RequestParam lock: Boolean): Boolean

    @ResponseBody
    @RequestMapping("/unlockAllRecordsForUser", method = [RequestMethod.POST])
    fun unlockAllRecordsForUser(@RequestParam user: String): String

    @ResponseBody
    @RequestMapping("/getUserByLogin", method = [RequestMethod.POST])
    fun getUserByLogin(@RequestParam login: String): User?

    @ResponseBody
    @RequestMapping("/clearMenus", method = [RequestMethod.POST])
    fun clearMenus(): String

    @ResponseBody
    @RequestMapping("/createOrUpdateMenuEntry", method = [RequestMethod.POST])
    fun createOrUpdateMenuEntry(@RequestBody menuEntryDto: MenuEntryDto, @RequestParam parentEntryId: String?): MenuEntryDto

    @ResponseBody
    @RequestMapping("/readNextLevel", method = [RequestMethod.POST])
    fun readNextLevel(@RequestParam url: String, @RequestBody authorities: List<String>): List<MenuEntryDto>

    @ResponseBody
    @RequestMapping("/readBreadcrumbs", method = [RequestMethod.POST])
    fun readBreadcrumbs(@RequestParam url: String): List<Breadcrumb>

    @ResponseBody
    @RequestMapping("/selectCachePage", method = [RequestMethod.POST])
    fun selectCachePage(@RequestParam page: Int, @RequestParam pageSize: Int, @RequestParam sortName: String, @RequestParam sortOrder: String, @RequestBody filterDto: List<FilterDto>, @RequestParam cacheName: String): List<Any>

    @ResponseBody
    @RequestMapping("/getContactsByPersonId", method = [RequestMethod.POST])
    fun getContactsByPersonId(@RequestParam id: String): List<ContactDto>

    @ResponseBody
    @RequestMapping("/getTotalDataSize", method = [RequestMethod.POST])
    fun getTotalDataSize(@RequestParam cacheName: String, @RequestBody filterDto: List<FilterDto>): Int

    @ResponseBody
    @RequestMapping("/notLockedByUser", method = [RequestMethod.POST])
    fun notLockedByUser(@RequestParam key: String, @RequestParam user: String): Boolean

    @ResponseBody
    @RequestMapping("/ifOrganizationExists", method = [RequestMethod.POST])
    fun ifOrganizationExists(@RequestParam key: String): Boolean

    @ResponseBody
    @RequestMapping("/ifPersonExists", method = [RequestMethod.POST])
    fun ifPersonExists(@RequestParam key: String): Boolean

    @ResponseBody
    @RequestMapping("/ifContactExists", method = [RequestMethod.POST])
    fun ifContactExists(@RequestParam key: String): Boolean
}
