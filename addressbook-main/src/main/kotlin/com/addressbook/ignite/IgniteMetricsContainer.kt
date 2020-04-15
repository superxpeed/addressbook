package com.addressbook.ignite

import com.addressbook.dto.IgniteMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*


@Component
class IgniteMetricsContainer : Serializable {

    @Autowired
    var igniteDao: IgniteClient? = null

    var igniteCacheMetricsMap: HashMap<String, IgniteMetrics>? = null

    fun refresh(): IgniteMetricsContainer {
        igniteCacheMetricsMap = HashMap()
        for (metricsEntry in igniteDao?.getCacheMetrics()?.entries!!) {
            igniteCacheMetricsMap?.put(metricsEntry.key, IgniteMetrics(metricsEntry.value))
        }
        return this
    }
}
