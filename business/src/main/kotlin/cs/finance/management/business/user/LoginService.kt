package cs.finance.management.business.user

import cs.finance.management.persistence.users.LoginEvent
import cs.finance.management.persistence.users.LoginEventRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import smile.clustering.dbscan
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit





@Service
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

    fun getPreviousLoginIps(userId: Long) : List<String> {
        return loginEventRepository.getPreviousLoginIps(userId)
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


    fun detectAnomalies(userId: Long): List<LocalDateTime> {
        val loginTimes = loginEventRepository.findLoginTimesByUserId(userId)
        val data = loginTimes.map { time ->
            doubleArrayOf(
                time.dayOfWeek.value.toDouble(),  // Wochentag (1-7)
                time.hour.toDouble() + time.minute.toDouble() / 60.0  // Uhrzeit als Dezimalzahl
            )
        }.toTypedArray()

        // Daten normalisieren
        val normalizedData = normalizeData(data)

        // Parameter für DBSCAN werden festgelegt
        val eps = 0.1  // Epsilon-Wert für normalisierte Daten
        val minPts = 3 // Mindestanzahl von Punkten in einem Cluster

        val dbscan = dbscan(normalizedData, minPts, eps)
        return dbscan.y
            .mapIndexed { index, cluster ->
                if (cluster == 2147483647) loginTimes[index] else null
            }
            .filterNotNull()
    }

    fun normalizeData(data: Array<DoubleArray>): Array<DoubleArray> {
        val minDay = 1.0
        val maxDay = 7.0
        val minTime = 0.0
        val maxTime = 24.0

        return data.map { point ->
            doubleArrayOf(
                (point[0] - minDay) / (maxDay - minDay),
                (point[1] - minTime) / (maxTime - minTime)
            )
        }.toTypedArray()
    }

    fun detectLoginTimeAnomalies(userId: Long): List<LocalDateTime> {
        return detectAnomalies(userId)
    }

    fun getLastSuccessfulLogin(userId: Long): LocalDateTime? {
        val pageRequest = PageRequest.of(0, 1)
        val lastLogin = loginEventRepository.findLastSuccessfulLoginByUserId(userId, pageRequest)
        return lastLogin.firstOrNull()?.loginTime
    }

}