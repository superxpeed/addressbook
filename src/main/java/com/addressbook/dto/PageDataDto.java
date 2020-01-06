package com.addressbook.dto;

import com.addressbook.model.FieldDescription;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class PageDataDto<T> {

    private T data;
    private Map<String, FieldDescription> fieldDescriptionMap;
}
