package cs.finance.management.business.logic

import cs.finance.management.persistence.users.LoginEvent
import cs.finance.management.persistence.users.LoginEventRepository
import cs.finance.management.persistence.users.UserRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import cs.finance.management.business.user.UserService
import org.springframework.context.ApplicationEvent
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent


@Component
class LoginListener(
    loginEventRepository: LoginEventRepository,
    private val userRepository: UserRepository,
    private val request: HttpServletRequest,
    private val userService: UserService,
    private val behaviourAnalysis: BehaviourAnalysis
) : ApplicationListener<ApplicationEvent> {
    private val loginEventRepository: LoginEventRepository = loginEventRepository


    @Transactional
    override fun onApplicationEvent(event: ApplicationEvent) {

        when (event){
            is AuthenticationSuccessEvent -> {
                val ipAddress = request.remoteAddr
                val user = userService.getAuthenticatedUser()
                val mail = user.mail
                val id = user.id
                val userAgent = request.getHeader("User-Agent") ?: "Unknown"
                val userAgentParsed = behaviourAnalysis.analyzeUserAgent(userAgent)
                //host, connection, sec-ch-ua, sec-ch-ua-mobile, sec-ch-ua-platform, origin, content-type. user-agent. referer

                val loginEvent = LoginEvent()
                loginEvent.loginTime = LocalDateTime.now()
                loginEvent.ipAddress = ipAddress
                loginEvent.location = "Frankfurt"
                loginEvent.userId = id
                loginEvent.mail = mail
                loginEvent.browser = userAgentParsed["browser"]
                loginEvent.browserVersion = userAgentParsed["browserVersion"]
                loginEvent.operatingSystem = userAgentParsed["operatingSystem"]
                loginEvent.status = "Success"
                loginEventRepository.save(loginEvent)
            }
            is AuthenticationFailureBadCredentialsEvent -> {
                val ipAddress = request.remoteAddr
                val mail = event.authentication.name
                val user = userService.findByMail(mail)
                val id = user?.id
                val userAgent = request.getHeader("User-Agent") ?: "Unknown"
                val userAgentParsed = behaviourAnalysis.analyzeUserAgent(userAgent)

                val loginEvent = LoginEvent()
                loginEvent.loginTime = LocalDateTime.now()
                loginEvent.ipAddress = ipAddress
                loginEvent.location = "Frankfurt"
                if (id != null) {
                    loginEvent.userId = id
                }
                loginEvent.mail = mail
                loginEvent.browser = userAgentParsed["browser"]
                loginEvent.browserVersion = userAgentParsed["browserVersion"]
                loginEvent.operatingSystem = userAgentParsed["operatingSystem"]
                loginEvent.status = "Failure"
                loginEventRepository.save(loginEvent)
            }
        }
    }
}
