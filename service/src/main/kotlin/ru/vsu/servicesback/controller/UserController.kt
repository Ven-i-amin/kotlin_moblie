package ru.vsu.servicesback.controller

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vsu.servicesback.dto.request.UserLoginRequest
import ru.vsu.servicesback.dto.request.UserRequest
import ru.vsu.servicesback.dto.response.UserResponse
import ru.vsu.servicesback.service.UserService

@Validated
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UserResponse =
        userService.getById(id)

    @GetMapping("/byEmail")
    fun getUserByEmail(
        @RequestParam(required = false) email: String?,
        @RequestBody(required = false) bodyEmail: String?,
    ): UserResponse = userService.getByEmail(resolveEmail(email, bodyEmail))

    @PostMapping("/log")
    fun login(@Valid @RequestBody request: UserLoginRequest): UserResponse =
        userService.login(request)

    @PostMapping("/reg")
    fun createUser(@Valid @RequestBody request: UserRequest) {
        userService.create(request)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @Valid @RequestBody request: UserRequest) {
        userService.update(id, request)
    }

    @PatchMapping("/{id}", "/{id}/balance")
    fun updateBalance(@PathVariable id: Long, @RequestBody request: String) {
        userService.changeBalance(id, extractDeltaBalance(request))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.delete(id)
    }

    private fun resolveEmail(email: String?, bodyEmail: String?): String =
        email?.takeIf { it.isNotBlank() }
            ?: bodyEmail?.trim()?.removeSurrounding("\"")?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Email is required")

    private fun extractDeltaBalance(request: String): Double {
        val normalized = request.trim()

        if (normalized.startsWith("{")) {
            val match = Regex(""""deltaBalance"\s*:\s*(-?\d+(?:\.\d+)?)""")
                .find(normalized)
                ?: throw IllegalArgumentException("deltaBalance is required")

            return match.groupValues[1].toDouble()
        }

        return normalized.removeSurrounding("\"").toDouble()
    }
}
