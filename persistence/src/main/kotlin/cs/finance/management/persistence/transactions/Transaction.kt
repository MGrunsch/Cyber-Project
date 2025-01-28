package cs.finance.management.persistence.transactions

import cs.finance.management.persistence.users.User
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class Transaction (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "sender_id")
    val sender: User,

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    val recipient: User,

    @Column(precision = 10, scale = 2)
    val amount: BigDecimal,

    @Column
    val timestamp: LocalDateTime = LocalDateTime.now()
)