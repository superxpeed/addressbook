package com.addressbook.rest;

import com.addressbook.model.Alert;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class WebAppErrorController implements ErrorController {

    @RequestMapping(path = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) return "404.html";
            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getWriter(), Alert.builder().headline("Error occurred!").type(Alert.DANGER).message("You are not authorized to see that content!").build());
            }
        }
        return null;
    }

    @Override
    public String getErrorPath() { return "/error"; }
}
