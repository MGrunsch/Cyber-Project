package cs.finance.management.persistence.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository: JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    fun findByMail(mail: String): User?

}