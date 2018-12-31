package com.addressbook.dto;

import com.addressbook.model.FieldDescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PageDataDto<T> {

    private T data;
    private Map<String, FieldDescription> fieldDescriptionMap;
}
