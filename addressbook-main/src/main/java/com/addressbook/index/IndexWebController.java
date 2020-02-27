package com.addressbook.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexWebController {

    @GetMapping("/")
    public String start() {
        return "index.html";
    }
}
