package cs.finance.management.persistence.users

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {

    fun findByMail(mail: String): User?

}