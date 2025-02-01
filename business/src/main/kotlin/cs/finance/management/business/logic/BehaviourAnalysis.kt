package cs.finance.management.business.logic

import cs.finance.management.business.user.LoginService
import cs.finance.management.business.user.UserService
import cs.finance.management.persistence.users.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.log

@Service
class BehaviourAnalysis (
    private val userService: UserService,
    private val loginService: LoginService
){

    fun analyzeUserAgent(userAgent: String): Map<String, String> {
        val browserRegex = Regex("(Chrome|Firefox|Safari|Edge|Opera)[/ ]([\\d.]+)")
        val osRegex = Regex("\\(([^)]+)\\)")

        val browserMatch = browserRegex.find(userAgent)
        val osMatch = osRegex.find(userAgent)

        val browser = browserMatch?.groupValues?.get(1) ?: "Unknown Browser"
        val browserVersion = browserMatch?.groupValues?.get(2) ?: "Unknown Version"
        val os = osMatch?.groupValues?.get(1) ?: "Unknown OS"

        return mapOf(
            "browser" to browser,
            "browserVersion" to browserVersion,
            "operatingSystem" to os
        )
    }

    fun checkIpAdress(ipAdress : String, riskScore : Int) : Int{
        var riskScore = riskScore
        val ips = loginService.getPreviousLoginIps()
        if (!ips.contains(ipAdress)){
            riskScore = riskScore + 20
        }
        return riskScore
    }

    // Ermittelt die Anzahl der fehlgeschlagenen Loginversuche innerhalb der letzten 30 Minuten
    fun getFailedLoginAttempts(riskScore: Int) : Int {
        val failedLoginAttempts = loginService.failedLoginAttempts().toInt()
        var riskScore = riskScore
        if (failedLoginAttempts == 0){
            riskScore += 0
        }
        else if (failedLoginAttempts == 1){
            riskScore += 10
        }
        else if (failedLoginAttempts == 2){
            riskScore += 20
        }
        else {
            riskScore += 40
        }
        return riskScore
    }


    fun checkLoginTime(riskScore: Int, userId: Long): Int {
        var updatedRiskScore = riskScore
        val currentLoginTime = loginService.getLastSuccessfulLogin(userId)

        val anomalies = loginService.detectLoginTimeAnomalies(userId)
        if (anomalies.contains(currentLoginTime)) {
            updatedRiskScore += 25
        }

        return updatedRiskScore
    }


    // Überprüft ob Kombination von Browser, Browser Version und Betriebssystem bereits bekannt sind
    fun checkBrowserConsistency(riskScore: Int, userId: Long, browserDetails: Map<String, String>): Int {
        var riskScore = riskScore
        val browser = browserDetails["browser"] ?: "Unknown"
        val browserVersion = browserDetails["browserVersion"] ?: "Unknown"
        val operatingSystem = browserDetails["operatingSystem"] ?: "Unknown"

        if (!loginService.areBrowserDetailsUsual(userId, browser, browserVersion, operatingSystem)) {
            riskScore += 15
        }

        return riskScore
    }


    // Ermitteln eines Risikowertes, basierend auf vorhandenen Daten wie IP Adresse, Anzahl der
    // fehlgeschlagenen Anmeldeversuche, Abweichungen von der normalen Login Zeit, Browser Daten und Betriebssystem
    fun calculateRiskScore(request: HttpServletRequest, username: String): Int {
        val user = userService.findByMail(username) ?: return 100 // Höchstes Risiko, wenn Benutzer nicht gefunden

        var riskScore = 0
        val ipAddress = request.remoteAddr
        val userAgent = request.getHeader("User-Agent") ?: "Unknown"
        val browserDetails = analyzeUserAgent(userAgent)

        riskScore = checkIpAdress(ipAddress, riskScore)
        riskScore = getFailedLoginAttempts(riskScore)
        riskScore = checkLoginTime(riskScore, user.id)
        riskScore = checkBrowserConsistency(riskScore, user.id, browserDetails)

        return riskScore
    }
}