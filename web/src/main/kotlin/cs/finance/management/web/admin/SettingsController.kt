package cs.finance.management.web.admin

import cs.finance.management.business.user.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@CrossOrigin(origins = ["*"], maxAge = 3600)
@Controller
class SettingsController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    @GetMapping("/settings")
    fun showSettings(model: Model): String {
        val user = userService.getAuthenticatedUser()

        model.addAttribute("userMail", user.mail)

        return "settings"
    }

    @PostMapping("/settings/changePassword")
    fun changePassword(
        @RequestParam currentPassword: String,
        @RequestParam newPassword: String,
        @RequestParam confirmPassword: String
    ): String {
        val user = userService.getAuthenticatedUser()

        if (!passwordEncoder.matches(currentPassword, user.password)) {
            return "redirect:/settings?error=wrongPassword"
        }

        if (newPassword != confirmPassword) {
            return "redirect:/settings?error=passwordMismatch"
        }

        user.passwd = passwordEncoder.encode(newPassword)
        userService.save(user)

        return "redirect:/settings?success=passwordChanged"
    }

}