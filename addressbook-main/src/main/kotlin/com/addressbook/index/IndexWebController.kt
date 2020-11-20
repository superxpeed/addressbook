package com.addressbook.index

import com.addressbook.annotations.LoggedGetRequest
import org.springframework.stereotype.Controller

@Controller
class IndexWebController {
    @LoggedGetRequest("/")
    fun start(): String {
        return "index.html"
    }
}
