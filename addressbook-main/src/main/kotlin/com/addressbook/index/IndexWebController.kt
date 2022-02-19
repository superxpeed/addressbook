package com.addressbook.index

import com.addressbook.annotations.LoggedGetRequest
import com.addressbook.dao.DaoClient
import com.addressbook.security.AuthRequest
import com.addressbook.security.AuthResponse
import com.addressbook.security.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@Controller
class IndexWebController {

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var dao: DaoClient

    @Autowired
    private lateinit var encoder: PasswordEncoder

    @LoggedGetRequest("/")
    fun start(): String {
        return "index.html"
    }

    @PostMapping("/auth")
    fun auth(@RequestBody request: AuthRequest): ResponseEntity<Any> {
        val user = request.login?.let { dao.getUserByLogin(it) }
                ?: return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED)
        if (!encoder.matches(request.password, user.password))
            return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED)
        return ResponseEntity(AuthResponse(jwtProvider.generateToken(request.login)), HttpStatus.OK)
    }
}
