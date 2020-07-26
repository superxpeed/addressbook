package com.addressbook.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestAwareAuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    private val sessionTimeoutInSeconds = 60 * 30

    private var requestCache: RequestCache = HttpSessionRequestCache()

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val savedRequest = requestCache.getRequest(request, response)
        request.session.maxInactiveInterval = sessionTimeoutInSeconds
        if (Objects.isNull(savedRequest)) {
            clearAuthenticationAttributes(request)
            return
        }
        val targetUrlParameter = targetUrlParameter
        if (isAlwaysUseDefaultTargetUrl || (Objects.nonNull(targetUrlParameter) && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response)
            clearAuthenticationAttributes(request)
            return
        }

        clearAuthenticationAttributes(request)
    }

    fun setRequestCache(requestCache: RequestCache) {
        this.requestCache = requestCache
    }
}
