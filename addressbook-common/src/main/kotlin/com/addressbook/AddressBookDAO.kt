package com.addressbook;

import com.addressbook.dto.*
import com.addressbook.model.Breadcrumb
import com.addressbook.model.User
import org.apache.ignite.cache.CacheMetrics
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

interface AddressBookDAO {
    @RequestMapping("/createOrUpdateOrganization")
    fun createOrUpdateOrganization(@RequestBody organizationDto: OrganizationDto, @RequestParam user: String): OrganizationDto

    @RequestMapping("/createOrUpdatePerson")
    fun createOrUpdatePerson(@RequestBody personDto: PersonDto, @RequestParam user: String): PersonDto

    @RequestMapping("/createOrUpdateContacts")
    fun createOrUpdateContacts(@RequestBody contactDtos: List<ContactDto>, @RequestParam user: String, @RequestParam personId: String): List<ContactDto>

    @RequestMapping("/createOrUpdateUser")
    fun createOrUpdateUser(@RequestBody newUser: User)

    @RequestMapping("/lockUnlockRecord")
    fun lockUnlockRecord(@RequestParam key: String, @RequestParam user: String, @RequestParam lock: Boolean): Boolean

    @RequestMapping("/unlockAllRecordsForUser")
    fun unlockAllRecordsForUser(@RequestParam user: String)

    @RequestMapping("/getUserByLogin")
    fun getUserByLogin(@RequestParam login: String): User?

    @RequestMapping("/clearMenus")
    fun clearMenus()

    @RequestMapping("/createOrUpdateMenuEntry")
    fun createOrUpdateMenuEntry(@RequestBody menuEntryDto: MenuEntryDto, @RequestParam parentEntryId: String?): MenuEntryDto

    @RequestMapping("/readNextLevel")
    fun readNextLevel(@RequestParam url: String, @RequestBody authorities: Collection<GrantedAuthority>): List<MenuEntryDto>

    @RequestMapping("/readBreadcrumbs")
    fun readBreadcrumbs(@RequestParam url: String): List<Breadcrumb>

    @RequestMapping("/selectCachePage")
    fun selectCachePage(@RequestParam page: Int, @RequestParam pageSize: Int, @RequestParam sortName: String, @RequestParam sortOrder: String, @RequestBody filterDto: List<FilterDto>, @RequestParam cacheName: String): List<Any>

    @RequestMapping("/getContactsByPersonId")
    fun getContactsByPersonId(@RequestParam id: String): List<ContactDto>

    @RequestMapping("/getTotalDataSize")
    fun getTotalDataSize(@RequestParam cacheName: String, @RequestBody filterDto: List<FilterDto>): Int

    @RequestMapping("/getCacheMetrics")
    fun getCacheMetrics(): Map<String, CacheMetrics>

    @RequestMapping("/notLockedByUser")
    fun notLockedByUser(@RequestParam key: String, @RequestParam user: String): Boolean

    @RequestMapping("/ifOrganizationExists")
    fun ifOrganizationExists(@RequestParam key: String): Boolean

    @RequestMapping("/ifPersonExists")
    fun ifPersonExists(@RequestParam key: String): Boolean

    @RequestMapping("/ifContactExists")
    fun ifContactExists(@RequestParam key: String): Boolean
}
