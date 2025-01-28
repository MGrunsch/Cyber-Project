package cs.finance.management

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@EnableScheduling
@SpringBootApplication(scanBasePackages = ["cs.finance.management.springconfig"])
class WebappApplication

fun main(args: Array<String>) {
	runApplication<WebappApplication>(*args)
}
