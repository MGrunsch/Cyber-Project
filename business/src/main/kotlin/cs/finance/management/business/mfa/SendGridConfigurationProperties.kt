package cs.finance.management.business.mfa

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

//@Validated
//@ConfigurationProperties(prefix = "spring.sendgrid")
//class SendGridConfigurationProperties {
//
//    @NotBlank
//    lateinit var apiKey: String
//
//    @Email
//    @NotBlank
//    lateinit var fromEmail: String
//
//    @NotBlank
//    lateinit var fromName: String
//}