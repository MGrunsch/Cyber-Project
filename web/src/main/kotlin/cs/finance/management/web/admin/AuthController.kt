package cs.finance.management.web.admin

import cs.finance.management.business.security.JwtTokenUtil
import cs.finance.management.business.security.MyUserDetailService
import cs.finance.management.business.user.UserService
import cs.finance.management.persistence.users.User
import cs.finance.management.web.admin.model.JwtResponse
import cs.finance.management.web.admin.model.LoginRequest
import cs.finance.management.web.admin.model.SignUpRequest
import cs.finance.management.web.admin.model.TransferRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType


@Controller
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenUtil: JwtTokenUtil,
    private var userDetailsService: MyUserDetailService
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


    @PostMapping("api/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        //val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        //val user = userService.getAuthenticatedUser()
        //val username = user.username
        val jwt = jwtTokenUtil.generateToken(authentication)
        val jwtResponse = JwtResponse(jwt)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(jwtResponse)
    }

    /*
    @PostMapping("api/auth/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        // Authentifizierung des Benutzers
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        // JWT erzeugen
        val jwt = jwtTokenUtil.generateToken(authentication)
        val jwtResponse = JwtResponse(jwt)

        // Weiterleitung nach erfolgreichem Login zum Dashboard
        val redirectUrl = "/dashboard" // Zieldestination für die Weiterleitung
        return ResponseEntity
            .status(HttpStatus.FOUND)  // Status 302 für Redirect
            .header(HttpHeaders.LOCATION, redirectUrl)  // Setzt den Location-Header
            .build()  // Ohne Body, da es sich um eine Weiterleitung handelt
    }*/


    @PostMapping("/api/auth/register")
    fun register(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        if (userService.findByMail(signUpRequest.username) != null) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!")
        }

        val user = User(
            mail = signUpRequest.username,
            passwd = passwordEncoder.encode(signUpRequest.password)
        )

        userService.save(user)

        return ResponseEntity.ok("User registered successfully!")
    }
}