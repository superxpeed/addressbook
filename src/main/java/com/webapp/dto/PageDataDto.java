package com.webapp.dto;

import com.webapp.model.FieldDescription;
import java.util.Map;

public class PageDataDto<T> {
    public static final PageDataDto EMPTY = new PageDataDto();

    private T data;
    private Map<String, FieldDescription> fieldDescriptionMap;

    public PageDataDto() {
        super();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setFieldDescriptionMap(Map<String,FieldDescription> fieldDescriptionMap) {
        this.fieldDescriptionMap = fieldDescriptionMap;
    }

    public Map<String, FieldDescription> getFieldDescriptionMap() {
        return fieldDescriptionMap;
    }
}
