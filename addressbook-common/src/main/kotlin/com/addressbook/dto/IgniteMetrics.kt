package com.addressbook.dto

import org.apache.ignite.cache.CacheMetrics
import java.io.Serializable

class IgniteMetrics constructor(cacheMetrics: CacheMetrics) : Serializable {
    var cacheGets: Long? = null
    var cachePuts: Long? = null
    var cacheRemovals: Long? = null
    var averageGetTime: Float? = null
    var averagePutTime: Float? = null
    var averageRemoveTime: Float? = null
    var offHeapGets: Long? = null
    var offHeapPuts: Long? = null
    var offHeapRemovals: Long? = null
    var heapEntriesCount: Long? = null
    var offHeapEntriesCount: Long? = null

    init {
        this.cacheGets = cacheMetrics.cacheGets
        this.cachePuts = cacheMetrics.cachePuts
        this.cacheRemovals = cacheMetrics.cacheRemovals
        this.averageGetTime = cacheMetrics.averageGetTime
        this.averagePutTime = cacheMetrics.averagePutTime
        this.averageRemoveTime = cacheMetrics.averageRemoveTime
        this.offHeapGets = cacheMetrics.offHeapGets
        this.offHeapPuts = cacheMetrics.offHeapPuts
        this.offHeapRemovals = cacheMetrics.offHeapRemovals
        this.heapEntriesCount = cacheMetrics.heapEntriesCount
        this.offHeapEntriesCount = cacheMetrics.offHeapEntriesCount
    }
}
