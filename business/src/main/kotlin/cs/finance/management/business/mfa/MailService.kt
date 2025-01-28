package cs.finance.management.business.mfa

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import cs.finance.management.business.user.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class MailService(
    private val sendGrid: SendGrid,
    private val otpService: OtpService
) {

    @Value("\${spring.sendgrid.from-email}")
    private lateinit var fromEmail: String

    @Value("\${spring.sendgrid.from-name}")
    private lateinit var fromName: String

    fun sendOTPEmail(userName: String) {
        val otp = otpService.generateOneTimePassword(userName)
        val body = String.format(
            OTP_MSG_TEMPLATE,
            userName, otp
        )
        sendEmail(OTP_TEMPLATE, body, userName)
    }

    fun sendEmail(subject: String, body: String, receiver: String?) {
        val from = Email(fromEmail, fromName)
        val to = Email(receiver)
        val content = Content("text/plain", body)
        val mail = Mail(from, subject, to, content)

        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            val response = sendGrid.api(request)
            if (response.statusCode !in 200..299) {
                throw IOException("SendGrid API error: ${response.statusCode} ${response.body}")
            }
        } catch (ex: IOException) {
            println("Error sending email: ${ex.message}")
        }
    }

    companion object {

        private const val OTP_TEMPLATE = "OTP Password"
        private val OTP_MSG_TEMPLATE =
            """
                Hello %s,${'\r'}
                it is required to enter a one time password:${'\r'}
                %s
                
                Best regards
                
                Your Finance Management Team
            """.trimIndent()
    }

}