package cs.finance.management.springconfig

import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootConfiguration
@EnableAsync
@ComponentScan("cs.finance.management.business")
class BusinessConfiguration