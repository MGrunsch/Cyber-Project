package cs.finance.management.web.admin

import cs.finance.management.business.logic.BehaviourAnalysis
import cs.finance.management.business.mfa.MailService
import cs.finance.management.business.mfa.OtpService
import cs.finance.management.business.security.JwtUtils
import cs.finance.management.business.security.MyUserDetailService
import cs.finance.management.business.user.UserService
import cs.finance.management.persistence.users.User
import cs.finance.management.persistence.users.UserRole
import cs.finance.management.web.admin.model.JwtResponse
import cs.finance.management.web.admin.model.LoginRequest
import cs.finance.management.web.admin.model.TransferRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtUtils: JwtUtils,
    private val behaviourAnalysis: BehaviourAnalysis,
    private val mailService: MailService,
    private val otpService: OtpService,
    private val myUserDetailService: MyUserDetailService
) {

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest, request: HttpServletRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = authentication.principal as UserDetails
        val riskScore = behaviourAnalysis.calculateRiskScore(request, loginRequest.username)

        if (riskScore in 0..39) {
            val user = userService.findByMail(loginRequest.username)
            if (user != null) {
                return ResponseEntity.ok(mapOf(
                    "requireSecurityQuestion" to true,
                    "securityQuestion" to user.securityQuestion
                ))
            }
        }

        if (riskScore >= 40){
            mailService.sendOTPEmail(loginRequest.username)
            return ResponseEntity.ok(mapOf("requireOTP" to true))
        }

        val jwt = jwtUtils.generateJwtToken(authentication)
        val roles = userDetails.authorities.map { it.authority }

        return ResponseEntity.ok(
            JwtResponse(
                jwt,
                email = userDetails.username,
                roles = roles
            )
        )
    }

    @PostMapping("/verify-otp")
    fun verifyOTP(@RequestBody otpRequest: OtpRequest): ResponseEntity<*> {
        val isValid = otpService.validateOTP(otpRequest.username, otpRequest.otp)
        if (isValid) {
            val userDetails = myUserDetailService.loadUserByUsername(otpRequest.username)
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtUtils.generateJwtToken(authentication)
            val roles = userDetails.authorities.map { it.authority }
            return ResponseEntity.ok(
                JwtResponse(
                    jwt,
                    email = userDetails.username,
                    roles = roles
                )
            )
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP")
    }

    data class OtpRequest(val username: String, val otp: String)



    @PostMapping("/transfer")
    fun transferMoney(
        @Valid @RequestBody transferRequest: TransferRequest
    ): Any {
        val authenticatedUser = userService.getAuthenticatedUser()
        val userRole = userService.findByMail(authenticatedUser.mail)?.role

        if (transferRequest.amount > BigDecimal(500) && userRole != UserRole.AUTHORIZED) {
            mailService.sendOTPEmail(authenticatedUser.mail)
            return ResponseEntity.ok(mapOf("requireOTP" to true))
        }

        userService.transferMoney(transferRequest.recipientId, transferRequest.amount)
        return "redirect:/dashboard"
    }


    @PostMapping("/verify-transfer-otp")
    fun verifyTransferOTP(@RequestBody transferRequest: TransferRequestWithOTP): String {
        val authenticatedUser = userService.getAuthenticatedUser()
        val isValidOTP = otpService.validateOTP(authenticatedUser.mail, transferRequest.otp)

        if (isValidOTP) {
            // Aktualisiere die Benutzerrolle
            userService.updateUserRole(authenticatedUser.mail, UserRole.AUTHORIZED)
            userService.transferMoney(transferRequest.recipientId, transferRequest.amount)

            return "redirect:/dashboard"
        }

        return "redirect:/dashboard"
    }

    @GetMapping("/roles")
    fun getUserRoles(): ResponseEntity<*> {
        val authenticatedUser = userService.getAuthenticatedUser()
        val role = userService.findByMail(authenticatedUser.mail)?.role
        return ResponseEntity.ok(mapOf("role" to role))
    }

    data class TransferRequestWithOTP(
        val recipientId: Long,
        val amount: BigDecimal,
        val otp: String
    )

    @PostMapping("/verify-security-question")
    fun verifySecurityQuestion(@RequestBody securityRequest: SecurityQuestionRequest): ResponseEntity<*> {
        val user = userService.findByMail(securityRequest.username)

        if (user != null && user.answer.equals(securityRequest.answer, ignoreCase = true)) {
            // Erfolgreiche Antwort, JWT erstellen
            val userDetails = myUserDetailService.loadUserByUsername(securityRequest.username)
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication

            // JWT erstellen
            val jwt = jwtUtils.generateJwtToken(authentication)
            val roles = userDetails.authorities.map { it.authority }

            // JWT und Benutzerrollen zurückgeben
            return ResponseEntity.ok(
                JwtResponse(
                    jwt,
                    email = userDetails.username,
                    roles = roles
                )
            )
        } else {
            // Fehlerhafte Antwort
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("valid" to false))
        }
    }

    data class SecurityQuestionRequest(val username: String, val answer: String)
}