package com.webapp.rest;

import com.webapp.GridUtils;
import com.webapp.UniversalFieldsDescriptor;
import com.webapp.dto.FilterDto;
import com.webapp.dto.PageDataDto;
import com.webapp.dto.TableDataDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @RequestMapping("/takeFullSnapshot")
    public PageDataDto<Float> takeFullSnapshot(){
        PageDataDto<Float> dto = new PageDataDto<>();
        dto.setData(GridUtils.takeFullSnapshot());
        return dto;
    }
}
