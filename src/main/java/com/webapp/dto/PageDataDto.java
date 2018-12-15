package com.webapp.dto;

import com.webapp.model.FieldDescription;
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
