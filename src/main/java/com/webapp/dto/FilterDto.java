package com.webapp.dto;

import java.io.Serializable;

public class FilterDto implements Serializable {
    private String name;
    private String value;
    private String comparator;
    private String type;

    public FilterDto(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FilterDto{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", comparator='" + comparator + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
