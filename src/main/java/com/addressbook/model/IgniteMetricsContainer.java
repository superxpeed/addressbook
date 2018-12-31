package com.addressbook.model;

import com.addressbook.ignite.GridDAO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ignite.cache.CacheMetrics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@Setter
public class IgniteMetricsContainer implements Serializable {

    private Map<String, IgniteCacheMetrics> igniteCacheMetricsMap;

    public IgniteMetricsContainer(){
        igniteCacheMetricsMap = new HashMap<>();
        for(Map.Entry<String, CacheMetrics> metricsEntry:  GridDAO.getCacheMetrics().entrySet()){
            igniteCacheMetricsMap.put(metricsEntry.getKey(), new IgniteCacheMetrics(metricsEntry.getValue()));
        }
    }
}
