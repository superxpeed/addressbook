package com.addressbook.index

import com.addressbook.aop.LogExecutionTime
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexWebController {
    @GetMapping("/")
    @LogExecutionTime
    fun start(): String {
        return "index.html"
    }
}
