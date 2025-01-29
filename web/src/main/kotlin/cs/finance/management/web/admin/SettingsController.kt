package cs.finance.management.web.admin

import cs.finance.management.business.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/settings")
class SettingsController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/verify-token")
    fun verifyHardwareToken(@RequestBody request: HardwareTokenRequest): ResponseEntity<*> {
        return if (request.token == "123456") {
            ResponseEntity.ok(mapOf("success" to true))
        } else {
            ResponseEntity.ok(mapOf("success" to false))
        }
    }

    @PostMapping("/change-password")
    fun changePassword(
        @RequestParam currentPassword: String,
        @RequestParam newPassword: String,
        @RequestParam confirmPassword: String,
        @RequestParam email: String
    ): ResponseEntity<*> {
        val user = userService.findByMail(email)!!

        if (!passwordEncoder.matches(currentPassword, user.password)) {
            return ResponseEntity.badRequest().body("Das aktuelle Passwort ist falsch.")
        }

        if (newPassword != confirmPassword) {
            return ResponseEntity.badRequest().body("Die neuen Passwörter stimmen nicht überein.")
        }

        user.passwd = passwordEncoder.encode(newPassword)
        userService.save(user)

        return ResponseEntity.ok("Passwort erfolgreich geändert.")
    }

    data class HardwareTokenRequest(val token: String)
}