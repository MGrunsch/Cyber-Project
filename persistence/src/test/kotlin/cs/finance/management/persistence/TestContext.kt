package cs.finance.management.persistence

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableJpaRepositories("cs.finance.management")
@EnableAutoConfiguration
class TestContext