package cs.finance.management.business.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtTokenUtil {
    @Value("\${jwt.secret}")
    private lateinit var secret: String


    private val JWT_EXPIRATION_MS = 86400000 // 24 Stunden

    //fun generateToken(authentication: Authentication): String {
    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetails
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + JWT_EXPIRATION_MS))
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()), SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.toByteArray()))
            .build()
            .parseClaimsJws(token)
            .body
        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build()
                .parseClaimsJws(token)
            return true
        } catch (ex: Exception) {
            return false
        }
    }
}
