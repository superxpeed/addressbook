package com.addressbook.index

import com.addressbook.annotations.LoggedGetRequest
import com.addressbook.dao.DaoClient
import com.addressbook.security.AuthRequest
import com.addressbook.security.AuthResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import com.addressbook.security.JwtProvider
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity


@Controller
class IndexWebController {

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var dao: DaoClient

    @LoggedGetRequest("/")
    fun start(): String {
        return "index.html"
    }

    @PostMapping("/auth")
    fun auth(@RequestBody request: AuthRequest): ResponseEntity<Any> {
        val user: com.addressbook.model.User = request.login?.let { dao.getUserByLogin(it) }
                ?: return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED)
        val encoder = BCryptPasswordEncoder()
        if (!encoder.matches(request.password, user.password))
            return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED)
        val token = jwtProvider.generateToken(request.login)
        return ResponseEntity(AuthResponse(token), HttpStatus.OK)
    }
}
