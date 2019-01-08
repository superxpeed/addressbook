package com.addressbook.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// IndexWebController methods do NOT require authorization
@Controller
public class IndexWebController {

    @RequestMapping("/")
    public String start() {
        return "index.html";
    }
}
