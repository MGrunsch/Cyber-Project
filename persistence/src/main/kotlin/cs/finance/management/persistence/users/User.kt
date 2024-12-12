package cs.finance.management.persistence.users

import cs.finance.management.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.NaturalId


@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    override var id: Long = 0,

    @NaturalId
    @Column(length = 255)
    @NotBlank
    val mail: String = "",

    @Column(length = 500)
    @NotBlank
    val password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(length = 13)
    var role: UserRole = UserRole.MITARBEITER,

    ) : HasIdOfType<Long>