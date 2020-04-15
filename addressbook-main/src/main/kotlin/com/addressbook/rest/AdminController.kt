package com.addressbook.rest

import com.addressbook.dto.JavaMetrics
import com.addressbook.ignite.IgniteMetricsContainer
import com.addressbook.services.IgniteStateService
import com.addressbook.services.JVMStateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Flux

@Controller
@RequestMapping(path = ["/rest/admin"])
class AdminController {

    @Autowired
    var jvmStateService: JVMStateService? = null

    @Autowired
    var igniteStateService: IgniteStateService? = null

    @GetMapping(value = ["/jvmState"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseBody
    fun jvmStateEvents(): Flux<JavaMetrics> {
        return jvmStateService!!.getJVMState()
    }

    @GetMapping(value = ["/igniteState"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseBody
    fun igniteMetricEvents(): Flux<IgniteMetricsContainer> {
        return igniteStateService!!.getIgniteState()
    }
}
