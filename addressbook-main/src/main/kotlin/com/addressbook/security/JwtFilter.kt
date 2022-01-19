package com.addressbook.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
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
        val token = getTokenFromRequest(servletRequest as HttpServletRequest)
        if (token != null && jwtProvider.validateToken(token)) {
            val userLogin: String = jwtProvider.getLoginFromToken(token)
            val customUserDetails: UserDetails = appUserDetailsService.loadUserByUsername(userLogin)
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.authorities)
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearer = request.getHeader(AUTHORIZATION)
        return if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            bearer.substring(7)
        } else null
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}