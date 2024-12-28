package cs.finance.management.web.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class RegistrationController {

    @GetMapping("/register")
    fun showRegistrationForm(): String {
        return "registration"
    }

    @PostMapping("/register")
    fun registerUser(): String {
        //TODO implement registration?
        return "redirect:/login"
    }
}