package com.addressbook.rest

import com.addressbook.UniversalFieldsDescriptor
import com.addressbook.annotations.LoggedGetRequest
import com.addressbook.annotations.LoggedPostRequest
import com.addressbook.dao.DaoClient
import com.addressbook.dto.*
import com.addressbook.exception.LockRecordException
import com.addressbook.model.Organization
import com.addressbook.model.Person
import com.addressbook.model.User
import com.addressbook.security.CurrentUser
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.CompletableFuture
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(path = ["/rest"])
class MainController {

    @Autowired
    lateinit var currentUser: CurrentUser

    @Autowired
    lateinit var daoDao: DaoClient

    @LoggedPostRequest("/getList4UniversalListForm")
    fun getList(@RequestParam(value = "start") start: Int,
                @RequestParam(value = "pageSize") pageSize: Int,
                @RequestParam(value = "sortName") sortName: String,
                @RequestParam(value = "sortOrder") sortOrder: String,
                @RequestParam(value = "cache") cache: String,
                @RequestBody filterDto: List<FilterDto>): CompletableFuture<PageDataDto<TableDataDto<Any>>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(TableDataDto(daoDao.selectCachePage(start, pageSize, sortName, sortOrder, filterDto, cache), daoDao.getTotalDataSize(cache, filterDto)), UniversalFieldsDescriptor.getFieldDescriptionMap(cache))
        }
    }

    @LoggedPostRequest("/getContactList")
    fun getContactList(@RequestParam(value = "personId") id: String): CompletableFuture<PageDataDto<TableDataDto<ContactDto>>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(TableDataDto(daoDao.getContactsByPersonId(id)))
        }
    }

    @LoggedPostRequest("/saveOrCreatePerson")
    fun saveOrCreatePerson(@RequestBody personDto: PersonDto): CompletableFuture<PageDataDto<PersonDto>> {
        val login = currentUser.userName
        if (daoDao.ifPersonExists(personDto.id!!) && daoDao.notLockedByUser(Person::class.java.name + personDto.id, login))
            throw LockRecordException("Parent record was not locked by $login")
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(daoDao.createOrUpdatePerson(personDto, login))
        }
    }

    @LoggedPostRequest("/saveOrCreateOrganization")
    fun saveOrCreateOrganization(@RequestBody organizationDto: OrganizationDto): CompletableFuture<PageDataDto<OrganizationDto>> {
        val login = currentUser.userName
        if (daoDao.ifOrganizationExists(organizationDto.id!!) && daoDao.notLockedByUser(Organization::class.java.name + organizationDto.id, login))
            throw LockRecordException("Record was not locked by $login")
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(daoDao.createOrUpdateOrganization(organizationDto, login))
        }
    }

    @LoggedGetRequest("/getBreadcrumbs")
    fun getBreadcrumbs(@RequestParam(value = "currentUrl") url: String): CompletableFuture<PageDataDto<List<Breadcrumb>>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(daoDao.readBreadcrumbs(url))
        }
    }

    @LoggedGetRequest("/getNextLevelMenus")
    fun getNextLevelMenus(@RequestParam(value = "currentUrl") url: String): CompletableFuture<PageDataDto<List<MenuEntryDto>>> {
        val authorities = currentUser.authorities.map { x -> x.authority }
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(daoDao.readNextLevel(url, authorities))
        }
    }

    @LoggedPostRequest("/saveOrCreateContacts")
    fun saveOrCreateContacts(@RequestBody contactDto: List<ContactDto>, @RequestParam(value = "personId") personId: String): CompletableFuture<PageDataDto<List<ContactDto>>> {
        val login = currentUser.userName
        if (daoDao.ifPersonExists(personId) && daoDao.notLockedByUser(Person::class.java.name + personId, login))
            throw LockRecordException("Parent record was not locked by $login")
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(daoDao.createOrUpdateContacts(contactDto, login, personId))
        }
    }

    @LoggedGetRequest("/lockRecord")
    fun lockRecord(@RequestParam(value = "type") type: String, @RequestParam(value = "id") id: String): PageDataDto<Alert> {
        val login = currentUser.userName
        return PageDataDto(if (daoDao.lockUnlockRecord(type + id, login, true))
            Alert("Record locked!", Alert.SUCCESS, Alert.RECORD_PREFIX + id + " locked!")
        else Alert("Record was not locked!", Alert.WARNING, Alert.RECORD_PREFIX + id + " was already locked!"))
    }

    @LoggedGetRequest("/unlockRecord")
    fun unlockRecord(@RequestParam(value = "type") type: String, @RequestParam(value = "id") id: String): PageDataDto<Alert> {
        val login = currentUser.userName
        return PageDataDto(if (daoDao.lockUnlockRecord(type + id, login, false))
            Alert("Record unlocked!", Alert.SUCCESS, Alert.RECORD_PREFIX + id + " unlocked!")
        else Alert("Record was not unlocked!", Alert.WARNING, Alert.RECORD_PREFIX + id + " was not locked by you!"))
    }

    @LoggedGetRequest("/logout")
    fun logoutPage(request: HttpServletRequest, response: HttpServletResponse): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            daoDao.unlockAllRecordsForUser(auth.name)
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return "redirect:/#/login"
    }

    @ExceptionHandler(*[Throwable::class])
    fun handleError(response: HttpServletResponse, ex: Throwable) {
        ex.printStackTrace()
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
        objectMapper.writeValue(response.writer, alert)
    }

    @LoggedGetRequest("/getUserInfo")
    fun getUserInfo(): User? {
        val user = daoDao.getUserByLogin(currentUser.userName)
        user?.password = null
        return user
    }
}
