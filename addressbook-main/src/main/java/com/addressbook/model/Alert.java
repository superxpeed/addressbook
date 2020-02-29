package com.addressbook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert implements Serializable {
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";
    public static final String DANGER = "danger";
    public static final String RECORD_PREFIX = "Record id ";
    private String headline;
    private String type;
    private String message;
}
