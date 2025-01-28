package cs.finance.management.web.admin

import cs.finance.management.business.security.JwtUtils
import cs.finance.management.business.security.MyUserDetailService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/auth")
class PushNotificationController(
    private val jwtUtils: JwtUtils,
    private val myUserDetailService: MyUserDetailService
) {

    @GetMapping("/push-notification")
    fun showPushNotification(@RequestParam username: String, model: Model): String {
        model.addAttribute("username", username)
        return "push-notification"
    }

    @PostMapping("/confirm-push")
    @ResponseBody
    fun confirmPush(@RequestParam username: String): ResponseEntity<*> {
        val userDetails = myUserDetailService.loadUserByUsername(username)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        val jwt = jwtUtils.generateJwtToken(authentication)
        return ResponseEntity.ok(mapOf("token" to jwt))
    }
}
