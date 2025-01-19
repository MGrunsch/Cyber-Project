package cs.finance.management.web.admin.model

import java.math.BigDecimal

data class LoginRequest(
    val username: String,
    val password: String
)

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: Set<String>? = null
)

data class TransferRequest(
    val recipientId: Long,
    val amount: BigDecimal
)


