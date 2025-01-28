package cs.finance.management.persistence.users

import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LoginEventRepository : JpaRepository<LoginEvent, Long> {
    @Query("SELECT COUNT(le) FROM LoginEvent le " +
            "WHERE le.status = 'Failure' " +
            "AND le.loginTime > :timeLimit")
    fun failedLoginAttempts(@Param("timeLimit") timeLimit: LocalDateTime): Long

    @Query("SELECT COUNT(le) > 0 FROM LoginEvent le WHERE le.ipAddress = :ipAddress AND le.status = 'Success'")
    fun findIp(@Param("ipAddress") ipAddress : String): Boolean

    @Query("SELECT le.loginTime FROM LoginEvent le WHERE le.userId = :userId AND le.status = 'Success'")
    fun findLoginTimesByUserId(@Param("userId") userId: Long): List<LocalDateTime>

    @Query("""
    SELECT COUNT(le) > 1
    FROM LoginEvent le WHERE le.userId = :userId AND le.browser = :browser AND le.browserVersion = :browserVersion 
    AND le.operatingSystem = :operatingSystem AND le.status = 'Success'
    """)
    fun areBrowserDetailsKnown(
        @Param("userId") userId: Long,
        @Param("browser") browser: String,
        @Param("browserVersion") browserVersion: String,
        @Param("operatingSystem") operatingSystem: String
    ): Boolean

    @Query("SELECT le FROM LoginEvent le WHERE le.userId = :userId AND le.status = 'Success' ORDER BY le.loginTime DESC")
    fun findLastSuccessfulLoginByUserId(@Param("userId") userId: Long, pageRequest: PageRequest): List<LoginEvent>
}
