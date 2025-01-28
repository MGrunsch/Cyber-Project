package cs.finance.management.web.admin

import cs.finance.management.business.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@CrossOrigin(origins = ["*"], maxAge = 3600)
class HistoryController(
    private val userService: UserService
) {

    @GetMapping("/history")
    fun showHistory(model: Model): String {

        val user = userService.getAuthenticatedUser()
        val transactions = userService.getTransactionHistory(user)
        model.addAttribute("transactions", transactions)

        return "history"
    }
}
