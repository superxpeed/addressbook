package com.addressbook.rest

import com.addressbook.IgniteStateService
import com.addressbook.JVMStateService
import com.addressbook.ignite.IgniteMetricsContainer
import com.addressbook.model.JVMState
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
    fun jvmStateEvents(): Flux<JVMState> {
        return jvmStateService!!.getJVMState()
    }

    @GetMapping(value = ["/igniteState"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseBody
    fun igniteMetricEvents(): Flux<IgniteMetricsContainer> {
        return igniteStateService!!.getIgniteState()
    }
}
