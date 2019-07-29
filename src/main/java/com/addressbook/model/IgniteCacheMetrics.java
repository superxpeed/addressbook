package com.addressbook.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ignite.cache.CacheMetrics;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class IgniteCacheMetrics implements Serializable {
    private long cacheGets;
    private long cachePuts;
    private long cacheRemovals;
    private float averageGetTime;
    private float averagePutTime;
    private float averageRemoveTime;
    private long offHeapGets;
    private long offHeapPuts;
    private long offHeapRemovals;
    private long heapEntriesCount;
    private long offHeapEntriesCount;

    public IgniteCacheMetrics(CacheMetrics cacheMetrics) {
        this.cacheGets = cacheMetrics.getCacheGets();
        this.cachePuts = cacheMetrics.getCachePuts();
        this.cacheRemovals = cacheMetrics.getCacheRemovals();

        this.averageGetTime = cacheMetrics.getAverageGetTime();
        this.averagePutTime = cacheMetrics.getAveragePutTime();
        this.averageRemoveTime = cacheMetrics.getAverageRemoveTime();

        this.offHeapGets = cacheMetrics.getOffHeapGets();
        this.offHeapPuts = cacheMetrics.getOffHeapPuts();
        this.offHeapRemovals = cacheMetrics.getOffHeapRemovals();

        this.heapEntriesCount = cacheMetrics.getHeapEntriesCount();
        this.offHeapEntriesCount = cacheMetrics.getOffHeapEntriesCount();
    }

}
