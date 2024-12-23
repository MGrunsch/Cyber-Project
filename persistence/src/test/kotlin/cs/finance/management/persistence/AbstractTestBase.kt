package cs.finance.management.persistence

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestContext::class])
@TestPropertySource(locations = ["classpath:application-test.properties"])
@EnableAutoConfiguration
@SpringJUnitConfig(initializers = [PostgresTestContextInitializer::class])
abstract class AbstractTestBase