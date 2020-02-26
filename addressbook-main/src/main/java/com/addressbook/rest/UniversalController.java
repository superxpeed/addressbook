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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/rest")
public class UniversalController {

    @Autowired
    private CurrentUser currentUser;

    @PostMapping("/getList4UniversalListForm")
    public CompletableFuture<PageDataDto<TableDataDto<?>>> getList(@RequestParam(value = "start") int start,
                                                                @RequestParam(value = "pageSize") int pageSize,
                                                                @RequestParam(value = "sortName") String sortName,
                                                                @RequestParam(value = "sortOrder") String sortOrder,
                                                                @RequestParam(value = "cache") String cache,
                                                                @RequestBody List<FilterDto> filterDto) {
        return CompletableFuture.supplyAsync(() ->
                PageDataDto.<TableDataDto<?>>builder()
                        .data(new TableDataDto<>(GridDAO.selectCachePage(start, pageSize, sortName, sortOrder, filterDto, cache), GridDAO.getTotalDataSize(cache, filterDto)))
                        .fieldDescriptionMap(UniversalFieldsDescriptor.getFieldDescriptionMap(cache)).build());
    }

    @GetMapping("/getContactList")
    public CompletableFuture<PageDataDto<TableDataDto<?>>> getContactList(@RequestParam(value = "personId") String id) {
        return CompletableFuture.supplyAsync(() -> PageDataDto.<TableDataDto<?>>builder().data(new TableDataDto<>(GridDAO.getContactsByPersonId(id))).build());
    }

    @PostMapping(value = "/saveOrCreatePerson")
    public CompletableFuture<PageDataDto<PersonDto>> saveOrCreatePerson(@RequestBody PersonDto personDto) {
        String login = currentUser.getCurrentUser();
        return CompletableFuture.supplyAsync(() -> PageDataDto.<PersonDto>builder().data(GridDAO.createOrUpdatePerson(personDto, login)).build());
    }

    @PostMapping(value = "/saveOrCreateOrganization")
    public CompletableFuture<PageDataDto<OrganizationDto>> saveOrCreateOrganization(@RequestBody OrganizationDto organizationDto) {
        String login = currentUser.getCurrentUser();
        return CompletableFuture.supplyAsync(() -> PageDataDto.<OrganizationDto>builder().data(GridDAO.createOrUpdateOrganization(organizationDto, login)).build());
    }

    @GetMapping(value = "/getBreadcrumbs")
    public CompletableFuture<PageDataDto<List<Breadcrumb>>> getBreadcrumbs(@RequestParam(value = "currentUrl") String url) {
        return CompletableFuture.supplyAsync(() -> PageDataDto.<List<Breadcrumb>>builder().data(GridDAO.readBreadcrumbs(url)).build());
    }

    @GetMapping(value = "/getNextLevelMenus")
    public CompletableFuture<PageDataDto<List<MenuEntryDto>>> getNextLevelMenus(@RequestParam(value = "currentUrl") String url) {
        Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
        return CompletableFuture.supplyAsync(() -> PageDataDto.<List<MenuEntryDto>>builder().data(GridDAO.readNextLevel(url, authorities)).build());
    }

    @PostMapping(value = "/saveOrCreateContacts")
    public CompletableFuture<PageDataDto<List<ContactDto>>> saveOrCreateContacts(@RequestBody List<ContactDto> contactDto, @RequestParam(value = "personId") String personId) {
        String login = currentUser.getCurrentUser();
        return CompletableFuture.supplyAsync(() -> PageDataDto.<List<ContactDto>>builder().data(GridDAO.createOrUpdateContacts(contactDto, login, personId)).build());
    }

    @GetMapping(value = "/lockRecord")
    public PageDataDto<Alert> lockRecord(@RequestParam(value = "type") String type, @RequestParam(value = "id") String id) {
        Alert alert = GridDAO.lockUnlockRecord(type + id, currentUser.getCurrentUser(), true)
                ? Alert.builder().headline("Record locked!").type(Alert.SUCCESS).message("Record id " + id + " locked!").build()
                : Alert.builder().headline("Record was not locked!").type(Alert.WARNING).message("Record id " + id + " was already locked!").build();
        return PageDataDto.<Alert>builder().data(alert).build();
    }

    @GetMapping(value = "/unlockRecord")
    public PageDataDto<Alert> unlockRecord(@RequestParam(value = "type") String type, @RequestParam(value = "id") String id) {
        Alert alert = GridDAO.lockUnlockRecord(type + id, currentUser.getCurrentUser(), false)
                ? Alert.builder().headline("Record unlocked!").type(Alert.SUCCESS).message("Record id " + id + " unlocked!").build()
                : Alert.builder().headline("Record was not unlocked!").type(Alert.WARNING).message("Record id " + id + " was not locked by you!").build();
        return PageDataDto.<Alert>builder().data(alert).build();
    }

    @GetMapping(value = "/logout")
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
        Alert alert = Alert.builder().type(Alert.DANGER).headline("Error occurred!").build();
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

    @GetMapping("/getUserInfo")
    public User getUserInfo() {
        User user = GridDAO.getUserByLogin(currentUser.getCurrentUser());
        user.setPassword(null);
        return user;
    }
}
