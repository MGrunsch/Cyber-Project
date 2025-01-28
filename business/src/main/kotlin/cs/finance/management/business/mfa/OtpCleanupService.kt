package cs.finance.management.business.mfa

import cs.finance.management.persistence.users.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
class OTPCleanupService(private val userRepository: UserRepository) {

    @Scheduled(fixedRate = 60000) // Läuft jede Minute
    fun cleanupExpiredOTPs() {
        val now = Date()
        val expiredUsers = userRepository.findByOtpExpiryTimeBefore(now)
        expiredUsers.forEach { user ->
            user.oneTimePassword = ""
            user.otpExpiryTime = null
            userRepository.save(user)
        }
    }
}
