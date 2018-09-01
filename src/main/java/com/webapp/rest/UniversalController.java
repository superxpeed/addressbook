package com.webapp.rest;

import com.webapp.GridUtils;
import com.webapp.UniversalFieldsDescriptor;
import com.webapp.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UniversalController{

    @ResponseBody
    @RequestMapping("/getList4UniversalListForm")
    public PageDataDto<TableDataDto> getList (@RequestParam(value = "start") int start,
                                              @RequestParam(value = "pageSize") int pageSize,
                                              @RequestParam(value = "sortName") String sortName,
                                              @RequestParam(value = "sortOrder") String sortOrder,
                                              @RequestParam(value = "cache") String cache,
                                              @RequestBody List<FilterDto> filterDto) {

        System.out.println(filterDto);

        TableDataDto td = new TableDataDto<>(GridUtils.selectCachePage(start,pageSize, sortName, sortOrder, filterDto, cache), GridUtils.getTotalDataSize(cache, filterDto));
        PageDataDto<TableDataDto> dto = new PageDataDto<>();
        dto.setData(td);
        dto.setTitle("Lol kek");
        dto.setBreadcrumbs(new ArrayList<>());
        dto.setFieldDescriptionMap(UniversalFieldsDescriptor.getFieldDescriptionMap(cache));
        return dto;
    }


    @ResponseBody
    @RequestMapping("/getContactList")
    public PageDataDto<TableDataDto> getContactList (@RequestParam(value = "personId") String id) {

        System.out.println("Person id: " + id);
        List<ContactDto> contactDtos = GridUtils.getContactsByPersonId(id);
        TableDataDto td = new TableDataDto<>(contactDtos, contactDtos.size());
        PageDataDto<TableDataDto> dto = new PageDataDto<>();
        dto.setData(td);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/saveOrCreatePerson", method = RequestMethod.POST)
    public PageDataDto<PersonDto> saveOrCreatePerson (@RequestBody PersonDto personDto) {
        System.out.println("PersonDto: " + personDto);
        PageDataDto<PersonDto> dto = new PageDataDto<>();
        dto.setData(GridUtils.createOrUpdatePerson(personDto));
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/saveOrCreateOrganization", method = RequestMethod.POST)
    public PageDataDto<OrganizationDto> saveOrCreateOrganization(@RequestBody OrganizationDto organizationDto) {
        System.out.println("OrganizationDto: " + organizationDto);
        PageDataDto<OrganizationDto> dto = new PageDataDto<>();
        dto.setData(GridUtils.createOrUpdateOrganization(organizationDto));
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/saveOrCreateContacts", method = RequestMethod.POST)
    public PageDataDto<List<ContactDto>> saveOrCreateContacts (@RequestBody List<ContactDto> contactDtos) {
        List<ContactDto> contactDtoList = new ArrayList<>();
        for(ContactDto contactDto: contactDtos){
            contactDtoList.add(GridUtils.createOrUpdateContact(contactDto));
        }
        PageDataDto<List<ContactDto>> dto = new PageDataDto<>();
        dto.setData(contactDtoList);
        return dto;
    }

    @ResponseBody
    @RequestMapping("/takeFullSnapshot")
    public PageDataDto<Float> takeFullSnapshot(){
        PageDataDto<Float> dto = new PageDataDto<>();
        dto.setData(GridUtils.takeFullSnapshot());
        return dto;
    }
}
