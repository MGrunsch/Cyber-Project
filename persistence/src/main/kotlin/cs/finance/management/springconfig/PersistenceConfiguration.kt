package cs.finance.management.springconfig

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@ComponentScan("cs.finance.management.persistence.*")
@Configuration
@EnableJpaRepositories("cs.finance.management.persistence.*")
@EntityScan("cs.finance.management.persistence.*")
class PersistenceConfiguration