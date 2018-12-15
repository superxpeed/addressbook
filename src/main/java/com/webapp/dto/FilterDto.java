package com.webapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FilterDto implements Serializable {

    private String name;
    private String value;
    private String comparator;
    private String type;

}
