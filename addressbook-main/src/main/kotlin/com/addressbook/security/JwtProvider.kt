package com.addressbook.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@Component
class JwtProvider {

    @Value("$(jwt.secret)")
    private lateinit var jwtSecret: String

    fun generateToken(login: String?): String {
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (e: MalformedJwtException) {
            //No-op
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getLoginFromToken(token: String?): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }
}