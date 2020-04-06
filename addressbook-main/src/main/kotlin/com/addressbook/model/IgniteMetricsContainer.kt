package com.addressbook.model

import com.addressbook.ignite.GridDAO;
import java.io.Serializable;
import java.util.HashMap;

class IgniteMetricsContainer : Serializable {

    var igniteCacheMetricsMap: HashMap<String, IgniteCacheMetrics>? = null

    init {
        igniteCacheMetricsMap = HashMap<String, IgniteCacheMetrics>()
        for (metricsEntry in GridDAO.getCacheMetrics().entries) {
            igniteCacheMetricsMap?.put(metricsEntry.key, IgniteCacheMetrics(metricsEntry.value))
        }
    }
}
