package com.addressbook.model

import com.addressbook.ignite.GridDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*


@Component
class IgniteMetricsContainer : Serializable {

    @Autowired
    var igniteDao: GridDAO? = null

    var igniteCacheMetricsMap: HashMap<String, IgniteCacheMetrics>? = null

    fun refresh(): IgniteMetricsContainer {
        igniteCacheMetricsMap = HashMap()
        for (metricsEntry in igniteDao?.getCacheMetrics()?.entries!!) {
            igniteCacheMetricsMap?.put(metricsEntry.key, IgniteCacheMetrics(metricsEntry.value))
        }
        return this
    }
}
