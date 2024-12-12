package cs.finance.management.business.user

import cs.finance.management.persistence.users.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
): UserRepository by userRepository {
}