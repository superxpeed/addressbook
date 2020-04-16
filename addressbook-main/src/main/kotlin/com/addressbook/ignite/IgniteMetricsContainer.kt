package com.addressbook.ignite

import com.addressbook.dto.IgniteMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class IgniteMetricsContainer : Serializable {

    @Autowired
    var igniteDao: IgniteClient? = null

    var igniteCacheMetricsMap: Map<String, IgniteMetrics>? = null

    fun refresh(): IgniteMetricsContainer {
        igniteCacheMetricsMap = igniteDao?.getCacheMetrics()
        return this
    }
}
