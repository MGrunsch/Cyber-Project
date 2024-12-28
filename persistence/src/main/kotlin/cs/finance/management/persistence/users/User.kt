package cs.finance.management.persistence.users

import cs.finance.management.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.NaturalId
import java.math.BigDecimal


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
    var password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(length = 13)
    var role: UserRole = UserRole.USER,

    @Column
    var enabled: Boolean = false,

    @Column(precision = 10, scale = 2)
    var budget: BigDecimal = BigDecimal.ZERO

) : HasIdOfType<Long>