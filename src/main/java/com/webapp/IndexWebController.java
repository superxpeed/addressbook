package com.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexWebController {

    @RequestMapping("/")
    public String start() {

        return "index.html";
    }
}
