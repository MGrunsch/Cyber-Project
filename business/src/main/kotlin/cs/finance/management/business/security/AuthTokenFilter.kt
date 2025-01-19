package cs.finance.management.business.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class AuthTokenFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: MyUserDetailService
) : OncePerRequestFilter() {

    companion object {
        private val loggi = LoggerFactory.getLogger(AuthTokenFilter::class.java)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            parseJwt(request)?.let { jwt ->
                if (jwtUtils.validateJwtToken(jwt)) {
                    val username = jwtUtils.getUserNameFromJwtToken(jwt)
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                    logger.info("Set authentication for user: $username")
                }
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: ${e.message}", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? =
        request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substring(7)
}
