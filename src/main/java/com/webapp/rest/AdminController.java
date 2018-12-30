package com.webapp.rest;

import com.webapp.IgniteStateService;
import com.webapp.model.IgniteMetricsContainer;
import com.webapp.model.JVMState;
import com.webapp.JVMStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping(path = "/rest/admin")
public class AdminController {

    @Autowired
    JVMStateService jvmStateService;

    @Autowired
    IgniteStateService igniteStateService;

    @GetMapping(value = "/jvmstate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<JVMState> jvmStateEvents(){
        return jvmStateService.getJVMState();
    }

    @GetMapping(value = "/ignitestate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<IgniteMetricsContainer> igniteMetricEvents(){
        return igniteStateService.getIgniteState();
    }
}
