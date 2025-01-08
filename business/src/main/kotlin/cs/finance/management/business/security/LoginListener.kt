package cs.finance.management.business.security

import cs.finance.management.persistence.users.LoginEvent
import cs.finance.management.persistence.users.LoginEventRepository
import cs.finance.management.persistence.users.UserRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import cs.finance.management.business.user.UserService


@Component
class LoginListener(
    loginEventRepository: LoginEventRepository,
    private val userRepository: UserRepository,
    private val request: HttpServletRequest,
    private val userService: UserService
) :
    ApplicationListener<AuthenticationSuccessEvent> {
    private val loginEventRepository: LoginEventRepository = loginEventRepository


    @Transactional
    override fun onApplicationEvent(event: AuthenticationSuccessEvent) {

        val ipAddress = request.remoteAddr
        //val user = userService.getAuthenticatedUser()
        //val mail = user.mail
        //val id = user.id
        //host, connection, sec-ch-ua, sec-ch-ua-mobile, sec-ch-ua-platform, origin, content-type. user-agent. referer


        //val location = getLocationFromIp(ipAddress)
        val location = "Test location"
        val loginEvent = LoginEvent()




        //loginEvent.userId = getUserIdFromAuthentication()
        loginEvent.loginTime = LocalDateTime.now()
        loginEvent.ipAddress = ipAddress
        loginEvent.location = "Frankfurt"
        loginEvent.userId = 123456
        loginEventRepository.save(loginEvent)
    }

    private fun getLocationFromIp(ipAddress: String): String {
        // Implementiere hier eine Logik, um den Standort anhand der IP-Adresse zu ermitteln
        return "Unknown" // Beispiel: Hier könnte eine externe API genutzt werden
    }
}

// Um Daten wie den Benutzernamen bei einem fehlgeschlagenen Login zu erhalten, kannst du
// stattdessen den AuthenticationFailureEvent oder eine AuthenticationFailureHandler verwenden.