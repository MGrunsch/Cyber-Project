package cs.finance.management.web.admin

import cs.finance.management.business.security.JwtTokenUtil
import cs.finance.management.business.user.UserService
import cs.finance.management.persistence.users.User
import cs.finance.management.web.admin.model.JwtResponse
import cs.finance.management.web.admin.model.LoginRequest
import cs.finance.management.web.admin.model.SignUpRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenUtil: JwtTokenUtil
){

    @GetMapping("")
    fun defaultRedirect(): String {
        return "login"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/register")
    fun register(): String {
        return "registration"
    }

    @PostMapping("/api/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtTokenUtil.generateToken(authentication)
        return ResponseEntity.ok(JwtResponse(jwt))
    }

    @PostMapping("/api/auth/register")
    fun register(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        if (userService.findByMail(signUpRequest.username) != null) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!")
        }

        val user = User(
            mail = signUpRequest.username,
            password = passwordEncoder.encode(signUpRequest.password)
        )

        userService.save(user)

        return ResponseEntity.ok("User registered successfully!")
    }
}