package com.addressbook.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Alert implements Serializable {

    private String headline;
    private String type;
    private String message;
}
