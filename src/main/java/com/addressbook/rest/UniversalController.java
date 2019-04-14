package com.addressbook.rest;

import com.addressbook.LockRecordException;
import com.addressbook.UniversalFieldsDescriptor;
import com.addressbook.dto.*;
import com.addressbook.ignite.GridDAO;
import com.addressbook.model.Alert;
import com.addressbook.model.Breadcrumb;
import com.addressbook.model.User;
import com.addressbook.security.CurrentUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

// All methods returns json in a response's body
@RestController
@RequestMapping(path = "/rest")
public class UniversalController {

    @Autowired
    private CurrentUser currentUser;

    // Because Ignite is fast, I decided to do all operations with datatable on server-side - pagination, sorting, searching
    @RequestMapping("/getList4UniversalListForm")
    public PageDataDto<TableDataDto> getList(@RequestParam(value = "start") int start,
                                             @RequestParam(value = "pageSize") int pageSize,
                                             @RequestParam(value = "sortName") String sortName,
                                             @RequestParam(value = "sortOrder") String sortOrder,
                                             @RequestParam(value = "cache") String cache,
                                             @RequestBody List<FilterDto> filterDto) {

        TableDataDto td = new TableDataDto<>(GridDAO.selectCachePage(start, pageSize, sortName, sortOrder, filterDto, cache), GridDAO.getTotalDataSize(cache, filterDto));
        PageDataDto<TableDataDto> dto = new PageDataDto<>();
        dto.setData(td);
        dto.setFieldDescriptionMap(UniversalFieldsDescriptor.getFieldDescriptionMap(cache));
        return dto;
    }


    // Simple REST API methods to save/retrieve information from Ignite DB
    @RequestMapping("/getContactList")
    public PageDataDto<TableDataDto> getContactList(@RequestParam(value = "personId") String id) {
        List<ContactDto> contactDtos = GridDAO.getContactsByPersonId(id);
        PageDataDto<TableDataDto> dto = new PageDataDto<>();
        dto.setData(new TableDataDto<>(contactDtos, contactDtos.size()));
        return dto;
    }

    @RequestMapping(value = "/saveOrCreatePerson", method = RequestMethod.POST)
    public PageDataDto<PersonDto> saveOrCreatePerson(@RequestBody PersonDto personDto) {
        PageDataDto<PersonDto> dto = new PageDataDto<>();
        dto.setData(GridDAO.createOrUpdatePerson(personDto, currentUser.getCurrentUser()));
        return dto;
    }

    @RequestMapping(value = "/saveOrCreateOrganization", method = RequestMethod.POST)
    public PageDataDto<OrganizationDto> saveOrCreateOrganization(@RequestBody OrganizationDto organizationDto) {
        PageDataDto<OrganizationDto> dto = new PageDataDto<>();
        dto.setData(GridDAO.createOrUpdateOrganization(organizationDto, currentUser.getCurrentUser()));
        return dto;
    }

    @RequestMapping(value = "/getBreadcrumbs", method = RequestMethod.GET)
    public PageDataDto<List<Breadcrumb>> getBreadcrumbs(@RequestParam(value = "currentUrl") String url) {
        PageDataDto<List<Breadcrumb>> dto = new PageDataDto<>();
        dto.setData(GridDAO.readBreadcrumbs(url));
        return dto;
    }

    @RequestMapping(value = "/getNextLevelMenus", method = RequestMethod.GET)
    public PageDataDto<List<MenuEntryDto>> getNextLevelMenus(@RequestParam(value = "currentUrl") String url) {
        PageDataDto<List<MenuEntryDto>> dto = new PageDataDto<>();
        dto.setData(GridDAO.readNextLevel(url, currentUser.getAuthorities()));
        return dto;
    }

    @RequestMapping(value = "/saveOrCreateContacts", method = RequestMethod.POST)
    public PageDataDto<List<ContactDto>> saveOrCreateContacts(@RequestBody List<ContactDto> contactDtos) {
        PageDataDto<List<ContactDto>> dto = new PageDataDto<>();
        dto.setData(GridDAO.createOrUpdateContacts(contactDtos, currentUser.getCurrentUser()));
        return dto;
    }


    // Pair of methods to lock and unlock table records to avoid concurrent modification by multiple users
    @RequestMapping(value = "/lockRecord", method = RequestMethod.GET)
    public PageDataDto<Alert> lockRecord(@RequestParam(value = "type") String type, @RequestParam(value = "id") String id) {
        // basically just puts in Ignite cache key/value pair, key is name of the cache and key of the record to be locked and value is username
        // returns true if current user successfully obtained lock on required record, otherwise it means
        // record was already locked by other user (I've decided not to return login of the user by whom this record was locked_
        boolean locked = GridDAO.lockUnlockRecord(type + id, currentUser.getCurrentUser(), true);
        Alert alert;
        if (locked) alert = new Alert("Record locked!", "success", "Record id " + id + " locked!");
        else alert = new Alert("Record was not locked!", "warning", "Record id " + id + " was already locked!");
        PageDataDto<Alert> dto = new PageDataDto<>();
        dto.setData(alert);
        return dto;
    }

    // Pretty much the same as lockRecord, difference in alerts messages
    @RequestMapping(value = "/unlockRecord", method = RequestMethod.GET)
    public PageDataDto<Alert> unlockRecord(@RequestParam(value = "type") String type, @RequestParam(value = "id") String id) {
        boolean unlocked = GridDAO.lockUnlockRecord(type + id, currentUser.getCurrentUser(), false);
        Alert alert;
        if (unlocked) alert = new Alert("Record unlocked!", "success", "Record id " + id + " unlocked!");
        else alert = new Alert("Record was not unlocked!", "warning", "Record id " + id + " was not locked by you!");
        PageDataDto<Alert> dto = new PageDataDto<>();
        dto.setData(alert);
        return dto;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // I need to clear all user locks
            GridDAO.unlockAllRecordsForUser(auth.getName());
            // and then logout (invalidate http-session and clear auth in current security context)
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        // Redirect to login form
        return "redirect:/#/login";
    }

    // Handles all (really all, bc Throwable) exceptions inside UniversalController
    @ExceptionHandler(Throwable.class)
    public void handleError(HttpServletResponse response, Throwable ex) throws Exception {
        // Let's mark all server-side exceptions with 500 status
        response.setStatus(500);
        // Basically all exceptions on server-side must be transformed to JSON, because
        // on client React code check if status is 200, and if not - displays response as user notifications
        // containing error description or stacktrace
        Alert alert = new Alert();
        // Type of user alert - red - so it's danger
        alert.setType("danger");
        alert.setHeadline("Error occurred!");

        // IllegalArgumentException in my code could only be thrown from  getBreadcrumbs
        // or getNextLevelMenus because such url doesn't exist
        if (ex.getClass().equals(IllegalArgumentException.class) || ex.getClass().equals(LockRecordException.class)) {
            // So here I just write exception's message as descriptions
            // Something like "Menu with url: " + url +  " doesn't exist"
            alert.setMessage(ex.getMessage());
        } else {
            // Or - just return full stacktrace of occurred exception
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            alert.setMessage(errors.toString());
        }
        // No automatic json marshalling, so we need to write it directly in response
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), alert);
    }

    // Returns user's info - name and roles
    @RequestMapping("/getUserInfo")
    public User getUserInfo() {
        // I could fill object from currentUser.getCurrentUser(), but it's easier to just read it
        User user = GridDAO.getUserByLogin(currentUser.getCurrentUser());
        // because object in DB contains password's hash and I don't want to return it to user
        user.setPassword(null);
        return user;
    }
}
