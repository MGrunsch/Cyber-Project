package cs.finance.management.web.admin.model

data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String
)
