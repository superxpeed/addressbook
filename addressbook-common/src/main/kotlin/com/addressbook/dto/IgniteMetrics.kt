package com.addressbook.dto

import java.io.Serializable

class IgniteMetrics : Serializable {
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
}
