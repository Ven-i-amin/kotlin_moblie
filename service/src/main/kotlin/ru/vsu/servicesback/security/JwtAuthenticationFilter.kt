package ru.vsu.servicesback.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.vsu.servicesback.service.UserService

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header = request.getHeader("Authorization")
        val token = header
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substringAfter("Bearer ")

        if (token != null && jwtService.isValid(token) && SecurityContextHolder.getContext().authentication == null) {
            val email = jwtService.extractEmail(token)
            val userEntity = userService.getEntityByEmailOrTokenId(email, jwtService.extractUserId(token))

            val principal = User
                .withUsername(userEntity.email)
                .password(userEntity.password)
                .roles("USER")
                .build()

            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.authorities,
            )
        }

        filterChain.doFilter(request, response)
    }
}
