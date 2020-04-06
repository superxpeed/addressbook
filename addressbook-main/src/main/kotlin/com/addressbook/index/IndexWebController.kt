package com.addressbook.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class IndexWebController {
    @GetMapping("/")
    fun start(): String {
        return "index.html"
    }
}
