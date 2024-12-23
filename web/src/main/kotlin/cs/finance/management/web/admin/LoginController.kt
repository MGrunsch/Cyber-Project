package cs.finance.management.web.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController(){

    @GetMapping("")
    fun defaultRedirect(): String {
        return "login"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }
}