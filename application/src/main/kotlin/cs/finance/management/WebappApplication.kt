package cs.finance.management

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["cs.finance.management.springconfig"])
class WebappApplication

fun main(args: Array<String>) {
	runApplication<WebappApplication>(*args)
}
