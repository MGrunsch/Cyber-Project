package cs.finance.management.persistence.users

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "login_events")
class LoginEvent {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var userId: Long = 1

    @Column(nullable = false)
    var loginTime: LocalDateTime? = null

    var ipAddress: String = ""

    var location: String? = null

}