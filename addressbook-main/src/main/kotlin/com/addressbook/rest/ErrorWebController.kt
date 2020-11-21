package com.addressbook.rest

import com.addressbook.dto.AlertDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class ErrorWebController : ErrorController {

    @RequestMapping(path = ["/error"], method = [RequestMethod.GET, RequestMethod.POST])
    fun handleError(request: HttpServletRequest, response: HttpServletResponse): String? {
        val status: Any = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        val statusCode = Integer.parseInt(status.toString())
        if (statusCode == HttpStatus.NOT_FOUND.value()) return "404.html"
        if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
            val objectMapper = ObjectMapper()
            objectMapper.writeValue(response.writer, AlertDto("Error occurred!", AlertDto.DANGER, "You are not authorized to see that content!"))
        }
        return null
    }

    override fun getErrorPath(): String {
        return "/error"
    }
}
