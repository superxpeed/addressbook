package com.addressbook.ignite

import com.addressbook.model.IgniteCacheMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*


@Component
class IgniteMetricsContainer : Serializable {

    @Autowired
    var igniteDao: IgniteDAOClient? = null;

    var igniteCacheMetricsMap: HashMap<String, IgniteCacheMetrics>? = null

    fun refresh(): IgniteMetricsContainer {
        igniteCacheMetricsMap = HashMap()
        for (metricsEntry in igniteDao?.getCacheMetrics()?.entries!!) {
            igniteCacheMetricsMap?.put(metricsEntry.key, IgniteCacheMetrics(metricsEntry.value))
        }
        return this
    }
}
