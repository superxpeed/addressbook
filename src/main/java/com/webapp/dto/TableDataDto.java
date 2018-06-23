package com.webapp.dto;

import java.util.List;

public class TableDataDto<T extends Object> {

    private List<T> data;

    public Integer getTotalDataSize() {
        return totalDataSize;
    }

    public void setTotalDataSize(Integer totalDataSize) {
        this.totalDataSize = totalDataSize;
    }

    private Integer totalDataSize;

    public TableDataDto(List<T> data, Integer totalDataSize) {
        this.data = data;
        this.totalDataSize = totalDataSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


}
