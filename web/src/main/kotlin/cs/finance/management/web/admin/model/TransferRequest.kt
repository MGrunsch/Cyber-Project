package cs.finance.management.web.admin.model

import java.math.BigDecimal

data class TransferRequest(
    val recipientMail: String,
    val amount: BigDecimal
)