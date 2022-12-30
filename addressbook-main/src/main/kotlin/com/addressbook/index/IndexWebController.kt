package com.addressbook.index

import com.addressbook.dao.DaoClient
import com.addressbook.security.JwtFilter
import com.addressbook.security.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.Serializable
import java.security.cert.X509Certificate
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

data class AuthRequest(val login: String? = null, val password: String? = null) : Serializable

data class AuthResponse(val token: String? = null) : Serializable

@Controller
class IndexWebController {

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var dao: DaoClient

    @Autowired
    private lateinit var encoder: PasswordEncoder

    @GetMapping("/")
    fun start(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): String {
        httpServletRequest.getAttribute("javax.servlet.request.X509Certificate")?.let { it ->
            if ((it as Array<*>).isNotEmpty()) {
                val cookies = httpServletRequest.cookies?.filter { cookie -> cookie.name.equals(JwtFilter.AUTHORIZATION) }
                if (cookies == null || cookies.isEmpty()) {
                    httpServletResponse.addCookie(Cookie(JwtFilter.AUTHORIZATION,
                            jwtProvider.generateToken((it[0] as X509Certificate).subjectX500Principal.name.replace("CN=", ""))))
                }
            }
        }
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

    @PostMapping("/rotateToken")
    fun renewToken(@RequestBody request: AuthResponse): ResponseEntity<Any> {
        if(!jwtProvider.validateToken(request.token))
            return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED)
        return ResponseEntity(AuthResponse(jwtProvider.generateToken(jwtProvider.getLoginFromToken(request.token))), HttpStatus.OK)
    }
}
