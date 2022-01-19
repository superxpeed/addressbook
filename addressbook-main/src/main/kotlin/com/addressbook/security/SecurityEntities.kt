package com.addressbook.security

import java.io.Serializable


data class AuthRequest(val login: String? = null, val password: String? = null) : Serializable

data class AuthResponse(val token: String? = null) : Serializable