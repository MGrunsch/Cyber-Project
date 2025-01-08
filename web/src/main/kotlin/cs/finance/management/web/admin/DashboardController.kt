package cs.finance.management.web.admin

import cs.finance.management.business.security.JwtTokenUtil
import cs.finance.management.business.user.UserService
import cs.finance.management.web.admin.model.TransferRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal

@Controller
class DashboardController(
    private val userService: UserService,
    private val jwtTokenUtil: JwtTokenUtil
) {

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        val user = userService.getAuthenticatedUser()
        val allUsers = userService.findAll()

        model.addAttribute("user", user)
        model.addAttribute("allUsers", allUsers)

        return "dashboard"
    }


    @PostMapping("/transfer")
    fun transferMoney(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @RequestBody transferRequest: TransferRequest
    ): ResponseEntity<*> {
        // Überprüfe, ob der Authorization Header existiert
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Fehler: Kein gültiger Token bereitgestellt.")
        }

        // Extrahiere den Token und überprüfe ihn
        val token = authorizationHeader.substring(7)
        if (!jwtTokenUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Fehler: Ungültiger Token.")
        }

        // Hole die Benutzerinformationen aus dem Token
        val username = jwtTokenUtil.getUsernameFromToken(token)
        val sender = userService.findByMail(username)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Fehler: Benutzer nicht gefunden.")

        // Überprüfe die Eingaben (Empfänger und Betrag)
        val recipient = userService.findByMail(transferRequest.recipientMail)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler: Empfänger nicht gefunden.")

        // Überprüfe, ob der Absender genug Guthaben hat (optional)
        if (sender.budget < transferRequest.amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler: Nicht genügend Guthaben.")
        }

        // Transferlogik (Budget anpassen)
        sender.budget -= transferRequest.amount
        recipient.budget += transferRequest.amount
        // Speichere die Änderungen
        userService.save(sender)
        userService.save(recipient)
        return ResponseEntity.ok("Überweisung erfolgreich.")
    }



/*
    @PostMapping("/transfer")
    fun transferMoney(
        @RequestParam recipientId: Long,
        @RequestParam amount: BigDecimal,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            userService.transferMoney(recipientId, amount)
            redirectAttributes.addFlashAttribute("successMessage", "Überweisung erfolgreich durchgeführt.")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", e.message)
        }
        return "redirect:/dashboard"
    }
 */

}