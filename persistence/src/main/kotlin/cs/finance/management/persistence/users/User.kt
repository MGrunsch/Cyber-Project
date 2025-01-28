package cs.finance.management.persistence.users

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.NaturalId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.math.BigDecimal
import java.time.Instant
import java.util.Date


@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var id: Long = 0,

    @NaturalId
    @Column(length = 255)
    @NotBlank
    val mail: String = "",

    @Column(name = "password",length = 500)
    @NotBlank
    var passwd: String = "",

    @Enumerated(EnumType.STRING)
    @Column(length = 13)
    var role: UserRole = UserRole.USER,

    @Column
    var enabled: Boolean = false,

    @Column(precision = 10, scale = 2)
    var budget: BigDecimal = BigDecimal.ZERO,

    @Column(name = "one_time_password")
    var oneTimePassword: String = "",

    @Column(name = "otp_request_time")
    var otpRequestTime: Date = Date.from(Instant.now().plusSeconds(600)),

    @Column(name = "otp_expiry_time")
    var otpExpiryTime: Date? = null,

    @Column(name = "phone_number", length = 20)
    var phoneNumber: String = "",

    // : HasIdOfType<Long>
) : UserDetails {

    override fun getUsername(): String = mail
    override fun getPassword(): String = passwd
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    fun getUserId(): Long = id

}
