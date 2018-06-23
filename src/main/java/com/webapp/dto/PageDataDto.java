package com.webapp.dto;

import com.webapp.model.Breadcrumb;
import com.webapp.model.FieldDescription;

import java.util.List;
import java.util.Map;

public class PageDataDto<T> {
    public static final PageDataDto EMPTY = new PageDataDto();

    private T data;
    private String title = "";
    private List<Breadcrumb> breadcrumbs;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Breadcrumb> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(List<Breadcrumb> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public void setFieldDescriptionMap(Map<String,FieldDescription> fieldDescriptionMap) {
        this.fieldDescriptionMap = fieldDescriptionMap;
    }

    public Map<String, FieldDescription> getFieldDescriptionMap() {
        return fieldDescriptionMap;
    }
}
