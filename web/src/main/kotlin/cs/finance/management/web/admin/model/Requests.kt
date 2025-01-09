package cs.finance.management.web.admin.model

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

