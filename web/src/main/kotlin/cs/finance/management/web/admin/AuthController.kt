package cs.finance.management.web.admin

import cs.finance.management.business.logic.BehaviourAnalysis
import cs.finance.management.business.mfa.MailService
import cs.finance.management.business.mfa.OtpService
import cs.finance.management.business.security.JwtUtils
import cs.finance.management.business.security.MyUserDetailService
import cs.finance.management.business.user.UserService
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

        if (riskScore >= 20 ) {
            otpService.generateOneTimePassword(loginRequest.username)
            val phoneNumber = userService.getCurrentUserPhoneNumber()
            val maskedPhoneNumber = maskPhoneNumber(phoneNumber ?: "")
            return ResponseEntity.ok(mapOf(
                "requireOTP" to true,
                "phoneNumber" to maskedPhoneNumber
            ))
        }

        if (riskScore in 10..20) {
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
    ): String {
        userService.transferMoney(transferRequest.recipientId, transferRequest.amount)
        return "redirect:/dashboard"
    }

    private fun maskPhoneNumber(phoneNumber: String): String {
        return phoneNumber.takeLast(4).padStart(phoneNumber.length, '*')
    }
}