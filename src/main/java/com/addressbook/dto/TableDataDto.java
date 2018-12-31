package com.addressbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TableDataDto<T> {

    private List<T> data;
    private Integer totalDataSize;

    public TableDataDto(List<T> data, Integer totalDataSize) {
        this.data = data;
        this.totalDataSize = totalDataSize;
    }
}
