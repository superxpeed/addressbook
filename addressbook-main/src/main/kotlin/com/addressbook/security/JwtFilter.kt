package com.addressbook.security

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


@Component
class JwtFilter : GenericFilterBean() {

    @Autowired
    lateinit var jwtProvider: JwtProvider

    @Autowired
    lateinit var appUserDetailsService: AppUserDetailsService

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        getTokenFromRequest(servletRequest as HttpServletRequest)?.let { s ->
            if (jwtProvider.validateToken(s)) {
                appUserDetailsService.loadUserByUsername(jwtProvider.getLoginFromToken(s)).also {
                    SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(it, null, it.authorities)
                }
            }
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