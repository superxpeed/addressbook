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

@RestController
@RequestMapping(path = "/rest")
public class UniversalController {

    @Autowired
    private CurrentUser currentUser;

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


    @RequestMapping(value = "/lockRecord", method = RequestMethod.GET)
    public PageDataDto<Alert> lockRecord(@RequestParam(value = "type") String type, @RequestParam(value = "id") String id) {
        boolean locked = GridDAO.lockUnlockRecord(type + id, currentUser.getCurrentUser(), true);
        Alert alert;
        if (locked) alert = new Alert("Record locked!", "success", "Record id " + id + " locked!");
        else alert = new Alert("Record was not locked!", "warning", "Record id " + id + " was already locked!");
        PageDataDto<Alert> dto = new PageDataDto<>();
        dto.setData(alert);
        return dto;
    }

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
            GridDAO.unlockAllRecordsForUser(auth.getName());
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/#/login";
    }

    @ExceptionHandler(Throwable.class)
    public void handleError(HttpServletResponse response, Throwable ex) throws Exception {
        response.setStatus(500);
        Alert alert = new Alert();
        alert.setType("danger");
        alert.setHeadline("Error occurred!");

        if (ex.getClass().equals(IllegalArgumentException.class) || ex.getClass().equals(LockRecordException.class)) {
            alert.setMessage(ex.getMessage());
        } else {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            alert.setMessage(errors.toString());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), alert);
    }

    @RequestMapping("/getUserInfo")
    public User getUserInfo() {
        User user = GridDAO.getUserByLogin(currentUser.getCurrentUser());
        user.setPassword(null);
        return user;
    }
}
