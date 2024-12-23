package cs.finance.management.persistence

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

open class PostgresTestContextInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

    private val DEFAULT_HOST = "localhost"
    private val DEFAULT_PORT = 35432
    private val DEFAULT_NAME = "webapp"
    private val DEFAULT_USERNAME = "db-it-user"
    private val DEFAULT_PASSWORD = "db-it-pass"

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        val dbHost: String = getEnvValue("POSTGRES_HOST", DEFAULT_HOST)
        val dbPort: String = getEnvValue("POSTGRES_PORT", DEFAULT_PORT.toString())
        val dbName: String = getEnvValue("POSTGRES_DB", DEFAULT_NAME)
        val dbUser: String = getEnvValue("POSTGRES_USER", DEFAULT_USERNAME)
        val dbPassword: String = getEnvValue("POSTGRES_PASSWORD", DEFAULT_PASSWORD)
        val dbUrl: String = dbUrl(dbHost, dbPort, dbName)
        TestPropertyValues.of(
            "spring.datasource.url=$dbUrl",
            "spring.datasource.username=$dbUser",
            "spring.datasource.password=$dbPassword"
        ).applyTo(configurableApplicationContext.environment)
        resetDBWithFlyway(dbUrl, dbUser, dbPassword)
    }

    open fun getEnvValue(envName: String, defaultValue: String): String {
        var envValue = System.getenv(envName)
        if (envValue == null) {
            envValue = defaultValue
        }
        return envValue
    }

    open fun resetDBWithFlyway(dbUrl: String, dbUser: String, dbPassword: String) {
        val config = ClassicConfiguration()
        config.setLocationsAsStrings("db/migration", "db/testdata")
        config.setDataSource(dbUrl, dbUser, dbPassword)
        config.isCleanDisabled = false
        val flyway = Flyway(config)
        flyway.clean()
        flyway.migrate()
    }

    open fun dbUrl(host: String, port: String, dbName: String): String {
        return "jdbc:postgresql://$host:$port/$dbName?loggerLevel=OFF"
    }

}