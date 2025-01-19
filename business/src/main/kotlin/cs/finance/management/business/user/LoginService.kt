package cs.finance.management.business.user

import cs.finance.management.persistence.users.LoginEvent
import cs.finance.management.persistence.users.LoginEventRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class LoginService (
    private val loginEventRepository: LoginEventRepository
) {

    fun failedLoginAttempts() : Long{
        val timeLimit = LocalDateTime.now().minus(30, ChronoUnit.MINUTES)
        return loginEventRepository.failedLoginAttempts(timeLimit)
        }


    fun checkIp(ip: String) : Boolean {
        return loginEventRepository.findIp(ip)
    }

    // Durschnittliches Zeitfenster ermitteln und Zeitraum um den Durschnitt herum legen
    fun analyzeLoginTimeWindow(userId: Long): Pair<Int, Int> {
        val loginTimes = loginEventRepository.findLoginTimesByUserId(userId)
        val loginHours = loginTimes.map { it.hour }

        // Durchschnittliche Login-Zeit berechnen (oder andere Logik verwenden)
        val averageHour = loginHours.average().toInt()

        // Definiere übliches Zeitfenster (zurzeit 2 Stunden um die durchschnittliche Login-Zeit)
        val startHour = (averageHour - 2).coerceAtLeast(0) // Minimum ist 0 Uhr
        val endHour = (averageHour + 2).coerceAtMost(23)  // Maximum ist 23 Uhr

        return Pair(startHour, endHour)
    }

    // Überprüfen, ob die aktuelle Login-Zeit im üblichen Zeitfenster liegt
    fun isLoginWithinUsualTimeWindow(userId: Long, loginTime: LocalDateTime): Boolean {
        val (startHour, endHour) = analyzeLoginTimeWindow(userId)
        val currentHour = loginTime.hour

        return currentHour in startHour..endHour
    }

    fun areBrowserDetailsUsual(userId: Long, browser: String, browserVersion: String, operatingSystem: String): Boolean {
        return loginEventRepository.areBrowserDetailsKnown(userId, browser, browserVersion, operatingSystem)
    }

}