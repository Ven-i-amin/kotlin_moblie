package ru.vsu.servicesback.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthContext {
    fun currentUserEmail(): String =
        SecurityContextHolder.getContext().authentication?.name
            ?: throw AccessDeniedException("Authentication is required")

    fun requireEmail(email: String) {
        if (currentUserEmail() != email) {
            throw AccessDeniedException("Access denied")
        }
    }
}
