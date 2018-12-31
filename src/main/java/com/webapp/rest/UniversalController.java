package com.webapp.rest;

import com.webapp.ignite.GridDAO;
import com.webapp.UniversalFieldsDescriptor;
import com.webapp.dto.*;
import com.webapp.model.Breadcrumb;
import com.webapp.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/rest")
public class UniversalController{

    @Autowired
    private CurrentUser currentUser;

    @RequestMapping("/getList4UniversalListForm")
    public PageDataDto<TableDataDto> getList (@RequestParam(value = "start") int start,
                                              @RequestParam(value = "pageSize") int pageSize,
                                              @RequestParam(value = "sortName") String sortName,
                                              @RequestParam(value = "sortOrder") String sortOrder,
                                              @RequestParam(value = "cache") String cache,
                                              @RequestBody List<FilterDto> filterDto) {

        TableDataDto td = new TableDataDto<>(GridDAO.selectCachePage(start,pageSize, sortName, sortOrder, filterDto, cache), GridDAO.getTotalDataSize(cache, filterDto));
        PageDataDto<TableDataDto> dto = new PageDataDto<>();
        dto.setData(td);
        dto.setFieldDescriptionMap(UniversalFieldsDescriptor.getFieldDescriptionMap(cache));
        return dto;
    }

    @RequestMapping("/getContactList")
    public PageDataDto<TableDataDto> getContactList (@RequestParam(value = "personId") String id) {
        List<ContactDto> contactDtos = GridDAO.getContactsByPersonId(id);
        TableDataDto td = new TableDataDto<>(contactDtos, contactDtos.size());
        PageDataDto<TableDataDto> dto = new PageDataDto<>();
        dto.setData(td);
        return dto;
    }

    @RequestMapping(value = "/saveOrCreatePerson", method = RequestMethod.POST)
    public PageDataDto<PersonDto> saveOrCreatePerson (@RequestBody PersonDto personDto) {
        PageDataDto<PersonDto> dto = new PageDataDto<>();
        dto.setData(GridDAO.createOrUpdatePerson(personDto));
        return dto;
    }

    @RequestMapping(value = "/saveOrCreateOrganization", method = RequestMethod.POST)
    public PageDataDto<OrganizationDto> saveOrCreateOrganization(@RequestBody OrganizationDto organizationDto) {
        PageDataDto<OrganizationDto> dto = new PageDataDto<>();
        dto.setData(GridDAO.createOrUpdateOrganization(organizationDto));
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
        dto.setData(GridDAO.readNextLevel(url, currentUser.getCurrentUser().getAuthorities()));
        return dto;
    }

    @RequestMapping(value = "/saveOrCreateContacts", method = RequestMethod.POST)
    public PageDataDto<List<ContactDto>> saveOrCreateContacts (@RequestBody List<ContactDto> contactDtos) {
        List<ContactDto> contactDtoList = new ArrayList<>();
        for(ContactDto contactDto: contactDtos){
            contactDtoList.add(GridDAO.createOrUpdateContact(contactDto));
        }
        PageDataDto<List<ContactDto>> dto = new PageDataDto<>();
        dto.setData(contactDtoList);
        return dto;
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/#/login";
    }

    @ExceptionHandler(Throwable.class)
    public void handleError(HttpServletResponse response, Exception ex) throws Exception{
        response.setStatus(500);
        ex.printStackTrace(response.getWriter());
    }
}
