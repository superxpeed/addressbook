package com.addressbook.rest

import com.addressbook.LockRecordException
import com.addressbook.UniversalFieldsDescriptor
import com.addressbook.dto.*
import com.addressbook.ignite.GridDAO
import com.addressbook.model.Alert
import com.addressbook.model.Breadcrumb
import com.addressbook.model.User
import com.addressbook.security.CurrentUser
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping(path = ["/rest"])
class UniversalController {

    @Autowired
    var currentUser: CurrentUser? = null

    @PostMapping("/getList4UniversalListForm")
    fun getList(@RequestParam(value = "start") start: Int,
                @RequestParam(value = "pageSize") pageSize: Int,
                @RequestParam(value = "sortName") sortName: String,
                @RequestParam(value = "sortOrder") sortOrder: String,
                @RequestParam(value = "cache") cache: String,
                @RequestBody filterDto: List<FilterDto>): CompletableFuture<PageDataDto<TableDataDto<Any>>> {
        return CompletableFuture.supplyAsync {
            val result = PageDataDto<TableDataDto<Any>>()
            result.data = TableDataDto(GridDAO.selectCachePage(start, pageSize, sortName, sortOrder, filterDto, cache), GridDAO.getTotalDataSize(cache, filterDto))
            result.fieldDescriptionMap = UniversalFieldsDescriptor.getFieldDescriptionMap(cache)
            return@supplyAsync result
        }
    }

    @PostMapping("/getContactList")
    fun getContactList(@RequestParam(value = "personId") id: String): CompletableFuture<PageDataDto<TableDataDto<ContactDto>>> {
        return CompletableFuture.supplyAsync {
            val result = PageDataDto<TableDataDto<ContactDto>>()
            result.data = TableDataDto(GridDAO.getContactsByPersonId(id))
            return@supplyAsync result
        }
    }

    @PostMapping("/saveOrCreatePerson")
    fun saveOrCreatePerson(@RequestBody personDto: PersonDto): CompletableFuture<PageDataDto<PersonDto>> {
        val login = currentUser?.getCurrentUser()!!
        return CompletableFuture.supplyAsync {
            val result = PageDataDto<PersonDto>()
            result.data = GridDAO.createOrUpdatePerson(personDto, login)
            return@supplyAsync result
        }
    }

    @PostMapping("/saveOrCreateOrganization")
    fun saveOrCreateOrganization(@RequestBody organizationDto: OrganizationDto): CompletableFuture<PageDataDto<OrganizationDto>> {
        val login = currentUser?.getCurrentUser()!!
        return CompletableFuture.supplyAsync {
            val result = PageDataDto<OrganizationDto>()
            result.data = GridDAO.createOrUpdateOrganization(organizationDto, login)
            return@supplyAsync result
        }
    }

    @GetMapping("/getBreadcrumbs")
    fun getBreadcrumbs(@RequestParam(value = "currentUrl") url: String): CompletableFuture<PageDataDto<List<Breadcrumb>>> {
        return CompletableFuture.supplyAsync {
            val result = PageDataDto<List<Breadcrumb>>()
            result.data = GridDAO.readBreadcrumbs(url)
            return@supplyAsync result
        }
    }

    @GetMapping("/getNextLevelMenus")
    fun getNextLevelMenus(@RequestParam(value = "currentUrl") url: String): CompletableFuture<PageDataDto<List<MenuEntryDto>>> {
        val authorities = currentUser?.authorities!!
        return CompletableFuture.supplyAsync {
            val result = PageDataDto<List<MenuEntryDto>>()
            result.data = GridDAO.readNextLevel(url, authorities)
            return@supplyAsync result
        }
    }

    @PostMapping("/saveOrCreateContacts")
    fun saveOrCreateContacts(@RequestBody contactDto: List<ContactDto>, @RequestParam(value = "personId") personId: String): CompletableFuture<PageDataDto<List<ContactDto>>> {
        val login = currentUser?.getCurrentUser()!!
        return CompletableFuture.supplyAsync {
            val result = PageDataDto<List<ContactDto>>()
            result.data = GridDAO.createOrUpdateContacts(contactDto, login, personId)
            return@supplyAsync result
        }
    }

    @GetMapping("/lockRecord")
    fun lockRecord(@RequestParam(value = "type") type: String, @RequestParam(value = "id") id: String): PageDataDto<Alert> {
        val result = PageDataDto<Alert>()
        result.data = if (GridDAO.lockUnlockRecord(type + id, currentUser?.getCurrentUser()!!, true))
            Alert("Record locked!", Alert.SUCCESS, Alert.RECORD_PREFIX + id + " locked!")
        else Alert("Record was not locked!", Alert.WARNING, Alert.RECORD_PREFIX + id + " was already locked!")
        return result
    }

    @GetMapping("/unlockRecord")
    fun unlockRecord(@RequestParam(value = "type") type: String, @RequestParam(value = "id") id: String): PageDataDto<Alert> {
        val result = PageDataDto<Alert>()
        result.data = if (GridDAO.lockUnlockRecord(type + id, currentUser?.getCurrentUser()!!, false))
            Alert("Record unlocked!", Alert.SUCCESS, Alert.RECORD_PREFIX + id + " unlocked!")
        else Alert("Record was not unlocked!", Alert.WARNING, Alert.RECORD_PREFIX + id + " was not locked by you!")
        return result
    }

    @GetMapping("/logout")
    fun logoutPage(request: HttpServletRequest, response: HttpServletResponse): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            GridDAO.unlockAllRecordsForUser(auth.name)
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return "redirect:/#/login"
    }

    @ExceptionHandler(*[Throwable::class])
    fun handleError(response: HttpServletResponse, ex: Throwable) {
        response.status = 500
        val alert = Alert()
        alert.headline = "Error occurred!"
        alert.type = Alert.DANGER
        if (ex.javaClass == IllegalArgumentException::class.java || ex.javaClass == LockRecordException::class.java) {
            alert.message = ex.message
        } else {
            val errors = StringWriter()
            ex.printStackTrace(PrintWriter(errors))
            alert.message = errors.toString()
        }
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(response.writer, alert);
    }

    @GetMapping("/getUserInfo")
    fun getUserInfo(): User? {
        val user = GridDAO.getUserByLogin(currentUser?.getCurrentUser()!!)
        user?.password = null
        return user;
    }
}
