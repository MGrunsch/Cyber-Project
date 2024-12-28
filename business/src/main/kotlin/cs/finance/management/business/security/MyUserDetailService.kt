package cs.finance.management.business.security

import cs.finance.management.persistence.users.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service("userDetailsService")
class MyUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByMail(username)
            ?: throw UsernameNotFoundException("User not found with email: $username")

        return User(user.mail, user.password, listOf(SimpleGrantedAuthority("ROLE_USER")))
    }
}