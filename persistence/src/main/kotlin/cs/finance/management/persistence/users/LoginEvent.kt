package cs.finance.management.persistence.users

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "login_events")
class LoginEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)

    var id: Long? = 3

    var userId: Long = 1

    @Column(nullable = false)
    var loginTime: LocalDateTime? = null

    var ipAddress: String = ""

    var location: String? = null

    var mail: String = ""

    var browser : String? = ""

    var browserVersion : String? = ""

    var operatingSystem : String? = ""

    var status = ""

}