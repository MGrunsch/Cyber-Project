package cs.finance.management.business.mfa

import cs.finance.management.persistence.users.UserRepository
import org.apache.commons.text.RandomStringGenerator
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class OtpService(
    private val userRepository: UserRepository
) {

    val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    fun generateOneTimePassword(userName: String): String {
        val user = userRepository.findByMail(userName)!!
        val otp = generateAlphanumericOTP()
        val encodedOTP = passwordEncoder.encode(otp)

        user.oneTimePassword = encodedOTP
        user.otpRequestTime = Date.from(Instant.now().plusSeconds(1200))
        user.otpExpiryTime = Date(System.currentTimeMillis() + 10 * 60 * 1000)

        userRepository.save(user)

        return otp
    }

    fun validateOTP(userName: String, otpCode: String): Boolean {
        val user = userRepository.findByMail(userName) ?: return false
        if (user.otpExpiryTime?.before(Date()) == true) return false
        return passwordEncoder.matches(otpCode, user.oneTimePassword)
    }

    fun generateAlphanumericOTP(length: Int = 8): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}