package cs.finance.management.web.admin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController{

    @GetMapping("/login")
    fun getHome(
        model: Model
    ): String {
        return "login"
    }
}