package cs.finance.management.web.admin

import cs.finance.management.business.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal

@Controller
class DashboardController(
    private val userService: UserService
) {

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        val user = userService.getAuthenticatedUser()
        val allUsers = userService.findAll()

        model.addAttribute("user", user)
        model.addAttribute("allUsers", allUsers)

        return "dashboard"
    }

    @PostMapping("/transfer")
    fun transferMoney(
        @RequestParam recipientId: Long,
        @RequestParam amount: BigDecimal,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            userService.transferMoney(recipientId, amount)
            redirectAttributes.addFlashAttribute("successMessage", "Überweisung erfolgreich durchgeführt.")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", e.message)
        }
        return "redirect:/dashboard"
    }
}