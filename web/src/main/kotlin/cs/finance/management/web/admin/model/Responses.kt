package cs.finance.management.web.admin.model

data class JwtResponse(
    val token: String,
    val type: String = "Bearer",
    val email: String,
    val roles: List<String>
)

data class MessageResponse(
    val message: String
)