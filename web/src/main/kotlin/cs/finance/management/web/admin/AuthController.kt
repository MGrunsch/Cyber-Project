package cs.finance.management.web.admin

import cs.finance.management.business.logic.BehaviourAnalysis
import cs.finance.management.business.security.JwtUtils
import cs.finance.management.business.user.UserService
import cs.finance.management.web.admin.model.JwtResponse
import cs.finance.management.web.admin.model.LoginRequest
import cs.finance.management.web.admin.model.TransferRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
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
) {

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest, request: HttpServletRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication


        //val riskScore = behaviourAnalysis.calculateRiskScore(request)

        val jwt = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetails
        val roles = userDetails.authorities.map { it.authority }

        return ResponseEntity.ok(
            JwtResponse(
                jwt,
                email = userDetails.username,
                roles = roles
            )
        )
    }

    @PostMapping("/transfer")
    fun transferMoney(
        @Valid @RequestBody transferRequest: TransferRequest
    ): String {
        userService.transferMoney(transferRequest.recipientId, transferRequest.amount)
        return "redirect:/dashboard"
    }
}