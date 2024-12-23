package cs.finance.management.persistence.it

import cs.finance.management.persistence.AbstractTestBase
import cs.finance.management.persistence.users.UserRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

class UserRepositoryIT: AbstractTestBase() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    @Transactional
    fun testFindByMail() {
        val user = userRepository.findByMail("testuser1@example.com")
        assertNotNull(user)
        assertEquals("testuser1@example.com", user?.mail)
    }
}