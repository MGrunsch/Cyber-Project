package cs.finance.management.business.security

import cs.finance.management.persistence.users.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class MyUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        return userRepository.findByMail(name)?.let { user ->
            val myUserDetails = MyUserDetails(user, "ROLE_${user.role.name}")

            myUserDetails

        }?: throw UsernameNotFoundException("Username or password is wrong for $name")
    }
}