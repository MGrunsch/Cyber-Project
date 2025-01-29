package cs.finance.management.business.user

import cs.finance.management.persistence.users.User
import cs.finance.management.persistence.users.UserRepository
import cs.finance.management.persistence.users.UserRole
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun getAuthenticatedUser(): User {
        val authenticationName = SecurityContextHolder.getContext().authentication.name
        return userRepository.findByMail(authenticationName)!!
    }

    fun findAll(): MutableList<User> {
        return userRepository.findAll()
    }

    fun findByMail(mail: String): User? {
        return userRepository.findByMail(mail)
    }

    @Transactional
    fun transferMoney(recipientId: Long, amount: BigDecimal) {
        val sender = getAuthenticatedUser()
        val recipient = userRepository.findById(recipientId).orElseThrow { IllegalArgumentException("Empfänger nicht gefunden") }

        if (sender.budget < amount) {
            throw IllegalStateException("Nicht genügend Guthaben für die Überweisung")
        }

        sender.budget = sender.budget.subtract(amount)
        recipient.budget = recipient.budget.add(amount)

        userRepository.save(sender)
        userRepository.save(recipient)
    }

    // Methode um Benutzerrolle zu aktualisieren
    @Transactional
    fun updateUserRole(mail: String, newRole: UserRole): User {
        // Benutzer anhand der E-Mail-Adresse finden
        val user = userRepository.findByMail(mail)
            ?: throw IllegalArgumentException("User not found with email: $mail")

        // Die Rolle des Benutzers aktualisieren
        user.role = newRole

        // Den aktualisierten Benutzer speichern
        return userRepository.save(user)
    }
}