package cs.finance.management.business.mfa

import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

//
//@Configuration
//@EnableConfigurationProperties(SendGridConfigurationProperties::class)
//@EnableTransactionManagement
//class SendGridConfiguration(
//    private val sendGridConfigurationProperties: SendGridConfigurationProperties
//) {
//
//    @Bean
//    fun sendGrid(): SendGrid {
//        val apiKey: String = sendGridConfigurationProperties.apiKey
//        return SendGrid(apiKey)
//    }
//
//    @Bean
//    fun fromEmail(): Email {
//        val fromEmail = sendGridConfigurationProperties.fromEmail
//        val fromName = sendGridConfigurationProperties.fromName
//        return Email(fromEmail, fromName)
//    }
//}