package com.addressbook.rest

import com.addressbook.FieldDescriptor
import com.addressbook.dao.DaoClient
import com.addressbook.dto.*
import com.addressbook.exceptions.LockRecordException
import com.addressbook.model.Organization
import com.addressbook.model.Person
import com.addressbook.model.User
import com.addressbook.security.AppUserDetails
import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*
import java.io.PrintWriter
import java.io.StringWriter
import java.net.InetAddress
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Timed
@RestController
@RequestMapping(path = ["/rest"])
class MainController {

    private val logger = LoggerFactory.getLogger(MainController::class.java)

    @Autowired
    lateinit var dao: DaoClient

    @Autowired
    lateinit var buildProperties: BuildProperties

    @PostMapping("/getList4UniversalListForm")
    fun getList(@RequestParam(value = "start") start: Int,
                @RequestParam(value = "pageSize") pageSize: Int,
                @RequestParam(value = "sortName") sortName: String,
                @RequestParam(value = "sortOrder") sortOrder: String,
                @RequestParam(value = "cache") cache: String,
                @RequestBody filterDto: List<FilterDto>): CompletableFuture<PageDataDto<TableDataDto<Any>>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(TableDataDto(dao.selectCachePage(start, pageSize, sortName, sortOrder, filterDto, cache), dao.getTotalDataSize(cache, filterDto)), FieldDescriptor.getFieldDescriptionMap(cache))
        }
    }

    @PostMapping("/getContactList")
    fun getContactList(@RequestParam(value = "personId") id: String): CompletableFuture<PageDataDto<TableDataDto<ContactDto>>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(TableDataDto(dao.getContactsByPersonId(id)))
        }
    }

    @PostMapping("/saveOrCreatePerson")
    fun saveOrCreatePerson(@RequestBody personDto: PersonDto, authentication: Authentication): CompletableFuture<PageDataDto<PersonDto>> {
        val login = (authentication.principal as AppUserDetails).username
        if (dao.ifPersonExists(personDto.id!!) && dao.notLockedByUser(Person::class.java.name + personDto.id, login))
            throw LockRecordException("Parent record was not locked by $login")
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(dao.createOrUpdatePerson(personDto, login))
        }
    }

    @GetMapping("/getOrganizationById")
    fun getOrganizationById(@RequestParam id: String): CompletableFuture<PageDataDto<OrganizationDto>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(dao.getOrganizationById(id))
        }
    }

    @GetMapping("/getPersonById")
    fun getPersonById(@RequestParam id: String): CompletableFuture<PageDataDto<PersonDto>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(dao.getPersonById(id))
        }
    }

    @PostMapping("/saveOrCreateOrganization")
    fun saveOrCreateOrganization(@RequestBody organizationDto: OrganizationDto, authentication: Authentication): CompletableFuture<PageDataDto<OrganizationDto>> {
        val login = (authentication.principal as AppUserDetails).username
        if (dao.ifOrganizationExists(organizationDto.id!!) && dao.notLockedByUser(Organization::class.java.name + organizationDto.id, login))
            throw LockRecordException("Record was not locked by $login")
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(dao.createOrUpdateOrganization(organizationDto, login))
        }
    }

    @GetMapping("/getBreadcrumbs")
    fun getBreadcrumbs(@RequestParam(value = "currentUrl") url: String): CompletableFuture<PageDataDto<List<BreadcrumbDto>>> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(dao.readBreadcrumbs(url))
        }
    }

    @GetMapping("/getNextLevelMenus")
    fun getNextLevelMenus(@RequestParam(value = "currentUrl") url: String, authentication: Authentication): CompletableFuture<PageDataDto<List<MenuEntryDto>>> {
        val authorities = (authentication.principal as AppUserDetails).authorities.map { x -> x.authority }
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(dao.readNextLevel(url, authorities))
        }
    }

    @GetMapping("/checkIfPageExists")
    fun checkIfPageExists(@RequestParam(value = "page") page: String): Boolean {
        return dao.ifPageExists(page)
    }

    @PostMapping("/saveOrCreateContacts")
    fun saveOrCreateContacts(@RequestBody contactDto: List<ContactDto>, @RequestParam(value = "personId") personId: String, authentication: Authentication): CompletableFuture<PageDataDto<List<ContactDto>>> {
        val login = (authentication.principal as AppUserDetails).username
        if (dao.ifPersonExists(personId) && dao.notLockedByUser(Person::class.java.name + personId, login))
            throw LockRecordException("Parent record was not locked by $login")
        return CompletableFuture.supplyAsync {
            return@supplyAsync PageDataDto(dao.createOrUpdateContacts(contactDto, login, personId))
        }
    }

    @GetMapping("/lockRecord")
    fun lockRecord(@RequestParam(value = "type") type: String, @RequestParam(value = "id") id: String, authentication: Authentication): PageDataDto<AlertDto> {
        val login = (authentication.principal as AppUserDetails).username
        return PageDataDto(if (dao.lockUnlockRecord(type + id, login, true))
            AlertDto("Record locked!", AlertDto.SUCCESS, AlertDto.RECORD_PREFIX + id + " locked!")
        else AlertDto("Record was not locked!", AlertDto.WARNING, AlertDto.RECORD_PREFIX + id + " was already locked!"))
    }

    @GetMapping("/unlockRecord")
    fun unlockRecord(@RequestParam(value = "type") type: String, @RequestParam(value = "id") id: String, authentication: Authentication): PageDataDto<AlertDto> {
        val login = (authentication.principal as AppUserDetails).username
        return PageDataDto(if (dao.lockUnlockRecord(type + id, login, false))
            AlertDto("Record unlocked!", AlertDto.SUCCESS, AlertDto.RECORD_PREFIX + id + " unlocked!")
        else AlertDto("Record was not unlocked!", AlertDto.WARNING, AlertDto.RECORD_PREFIX + id + " was not locked by you!"))
    }

    @GetMapping("/logout")
    fun logoutPage(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): String {
        val auth = authentication.principal as AppUserDetails
        dao.unlockAllRecordsForUser(auth.username)
        SecurityContextLogoutHandler().logout(request, response, authentication)
        return "redirect:/#/login"
    }

    @ExceptionHandler(Throwable::class)
    fun handleError(response: HttpServletResponse, ex: Throwable) {
        logger.error("Exception occurred:", ex)
        response.status = 500
        val alert = AlertDto()
        alert.headline = "Error occurred!"
        alert.type = AlertDto.DANGER
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

    @GetMapping("/getUserInfo")
    fun getUserInfo(authentication: Authentication): User? {
        val user = dao.getUserByLogin(((authentication.principal as AppUserDetails).username))
        user?.password = ""
        return user
    }

    @GetMapping("/getBuildInfo")
    fun getBuildInfo(): BuildInfoDto? {
        val buildInfoDto = BuildInfoDto()
        with(buildInfoDto) {
            version = buildProperties.version
            artifact = buildProperties.artifact
            group = buildProperties.group
            name = buildProperties.name
            serverHost = InetAddress.getLocalHost().hostName
            time = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss")
                    .withZone(ZoneId.systemDefault())
                    .format(buildProperties.time)
        }
        return buildInfoDto
    }
}
