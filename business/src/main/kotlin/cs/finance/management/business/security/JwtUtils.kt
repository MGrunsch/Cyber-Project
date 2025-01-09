package cs.finance.management.business.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtils {
    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.accessTokenExpiration}")
    private var jwtExpirationMs: Int = 0

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetails

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun key(): Key {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken)
            return true
        } catch (e: Exception) {
            when (e) {
                is io.jsonwebtoken.security.SecurityException,
                is io.jsonwebtoken.MalformedJwtException -> logger.error("Invalid JWT token: {}", e.message)
                is io.jsonwebtoken.ExpiredJwtException -> logger.error("JWT token is expired: {}", e.message)
                is io.jsonwebtoken.UnsupportedJwtException -> logger.error("JWT token is unsupported: {}", e.message)
                is IllegalArgumentException -> logger.error("JWT claims string is empty: {}", e.message)
            }
        }
        return false
    }
}