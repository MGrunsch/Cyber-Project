package cs.finance.management.web.admin

import cs.finance.management.business.user.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal

@CrossOrigin(origins = ["*"], maxAge = 3600)
@Controller
class DashboardController(
    private val userService: UserService
) {

    @GetMapping("finance-management")
    fun allAccess(): String {
        return "login"
    }

    @GetMapping("/register")
    fun registerPage(): String {
        return "registration"
    }

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        val user = userService.getAuthenticatedUser()
        val allUsers = userService.findAll()

        model.addAttribute("user", user)
        model.addAttribute("allUsers", allUsers)

        return "dashboard"
    }

    @GetMapping("/settings")
    fun showSettings(model: Model): String {
        val user = userService.getAuthenticatedUser()

        model.addAttribute("userMail", user.mail)

        return "settings"
    }
}