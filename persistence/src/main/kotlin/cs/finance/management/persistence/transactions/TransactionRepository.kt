package cs.finance.management.persistence.transactions

import cs.finance.management.persistence.users.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {

    @Transactional(readOnly = true)
    fun findBySenderOrRecipientOrderByTimestampDesc(sender: User, recipient: User): List<Transaction>
}