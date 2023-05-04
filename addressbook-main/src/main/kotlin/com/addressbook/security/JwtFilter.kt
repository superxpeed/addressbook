package com.addressbook.security

import com.addressbook.dto.AlertDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtFilter : GenericFilterBean() {

    @Autowired
    lateinit var jwtProvider: JwtProvider

    @Autowired
    lateinit var appUserDetailsService: AppUserDetailsService

    override fun doFilter(servletRequest: ServletRequest,
                          servletResponse: ServletResponse,
                          filterChain: FilterChain) {
        try {
            getTokenFromRequest(servletRequest as HttpServletRequest)?.let { s ->
                if (jwtProvider.validateToken(s)) {
                    appUserDetailsService.loadUserByUsername(jwtProvider.getLoginFromToken(s)).also {
                        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(it, null, it.authorities)
                    }
                }
            }
        } catch (ex: Exception) {
            (servletResponse as HttpServletResponse).status = 500
            ObjectMapper().writeValue(servletResponse.writer, AlertDto("Error occurred!", AlertDto.DANGER, ex.message))
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        request.getHeader(AUTHORIZATION).also {
            return if (StringUtils.hasText(it) && it.startsWith("Bearer ")) {
                it.substring(7)
            } else null
        }
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}