package com.vente_en_ligne.plateforme_e_commerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vente_en_ligne.plateforme_e_commerce.service.SmsNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import com.vente_en_ligne.plateforme_e_commerce.service.EmailService;
import com.vente_en_ligne.plateforme_e_commerce.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller REST pour les opérations de paiement
 */
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final CommandeService commandeService;
    private final EmailService emailService;
    private final SmsNotificationService smsService;

    /**
     * Obtenir les méthodes de paiement disponibles
     */
    @GetMapping("/methods")
    public ResponseEntity<List<Map<String, String>>> getPaymentMethods() {
        List<Map<String, String>> methods = Stream.of(PaymentService.PaymentMethod.values())
                .map(method -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", method.name());
                    map.put("name", method.getDisplayName());
                    map.put("description", method.getDescription());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(methods);
    }

    /**
     * Initier un paiement pour une commande
     */
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody Map<String, Object> request) {
        try {
            Long orderId = Long.valueOf(request.get("orderId").toString());
            String paymentMethod = request.get("paymentMethod").toString();
            String phoneNumber = request.get("phoneNumber") != null
                    ? request.get("phoneNumber").toString() : null;

            Commande commande = commandeService.obtenirCommandeParId(orderId);

            PaymentService.PaymentMethod method = PaymentService.PaymentMethod.valueOf(paymentMethod);

            // Valider le numéro de téléphone pour les paiements mobiles
            if (method != PaymentService.PaymentMethod.CASH_ON_DELIVERY && phoneNumber != null) {
                boolean isValid = false;
                switch (method) {
                    case WAVE:
                        isValid = paymentService.validateWavePhoneNumber(phoneNumber);
                        break;
                    case ORANGE_MONEY:
                        isValid = paymentService.validateOrangePhoneNumber(phoneNumber);
                        break;
                    case FREE_MONEY:
                        isValid = paymentService.validateFreePhoneNumber(phoneNumber);
                        break;
                }
                if (!isValid) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "message", "Numéro de téléphone invalide pour " + method.getDisplayName()
                    ));
                }
            }

            Map<String, Object> paymentResult = paymentService.initiatePayment(commande, method, phoneNumber);

            // Mettre à jour le statut de la commande
            commande.setStatut("PAYMENT_PENDING");
            commandeService.mettreAJourStatutCommande(orderId,
                    com.vente_en_ligne.plateforme_e_commerce.entity.StatutCommande.EN_ATTENTE);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "payment", paymentResult
            ));

        } catch (IllegalArgumentException e) {
            log.error("Erreur méthode de paiement: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Méthode de paiement non valide"
            ));
        } catch (Exception e) {
            log.error("Erreur initiation paiement: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Erreur lors de l'initiation du paiement"
            ));
        }
    }

    /**
     * Callback pour les paiements Wave
     */
    @PostMapping("/wave/callback")
    public ResponseEntity<?> waveCallback(@RequestBody Map<String, Object> callbackData) {
        log.info("Callback Wave reçu: {}", callbackData);

        boolean success = paymentService.processWaveCallback(callbackData);

        if (success) {
            String orderId = callbackData.get("order_id").toString();
            commandeService.mettreAJourStatutCommande(Long.parseLong(orderId),
                    com.vente_en_ligne.plateforme_e_commerce.entity.StatutCommande.CONFIRMEE);

            // Envoyer email de confirmation au propriétaire
            try {
                Commande commande = commandeService.obtenirCommandeParId(Long.parseLong(orderId));
                emailService.envoyerNotificationCommandeAuProprietaire(commande);
            } catch (Exception e) {
                log.error("Erreur envoi email: {}", e.getMessage());
            }
        }

        return ResponseEntity.ok(Map.of("status", success ? "SUCCESS" : "FAILED"));
    }

    /**
     * Callback pour les paiements Orange Money
     */
    @PostMapping("/orange/callback")
    public ResponseEntity<?> orangeCallback(@RequestBody Map<String, Object> callbackData) {
        log.info("Callback Orange Money reçu: {}", callbackData);

        boolean success = paymentService.processOrangeCallback(callbackData);

        if (success) {
            String orderId = callbackData.get("order_id").toString();
            commandeService.mettreAJourStatutCommande(Long.parseLong(orderId),
                    com.vente_en_ligne.plateforme_e_commerce.entity.StatutCommande.CONFIRMEE);

            try {
                Commande commande = commandeService.obtenirCommandeParId(Long.parseLong(orderId));
                emailService.envoyerNotificationCommandeAuProprietaire(commande);
            } catch (Exception e) {
                log.error("Erreur envoi email: {}", e.getMessage());
            }
        }

        return ResponseEntity.ok(Map.of("status", success ? "SUCCESS" : "FAILED"));
    }

    /**
     * Callback pour les paiements Free Money
     */
    @PostMapping("/free/callback")
    public ResponseEntity<?> freeCallback(@RequestBody Map<String, Object> callbackData) {
        log.info("Callback Free Money reçu: {}", callbackData);

        boolean success = paymentService.processFreeCallback(callbackData);

        if (success) {
            String orderId = callbackData.get("order_id").toString();
            commandeService.mettreAJourStatutCommande(Long.parseLong(orderId),
                    com.vente_en_ligne.plateforme_e_commerce.entity.StatutCommande.CONFIRMEE);

            try {
                Commande commande = commandeService.obtenirCommandeParId(Long.parseLong(orderId));
                emailService.envoyerNotificationCommandeAuProprietaire(commande);
            } catch (Exception e) {
                log.error("Erreur envoi email: {}", e.getMessage());
            }
        }

        return ResponseEntity.ok(Map.of("status", success ? "SUCCESS" : "FAILED"));
    }

    /**
     * Vérifier le statut d'un paiement
     */
    @GetMapping("/status")
    public ResponseEntity<?> checkPaymentStatus(
            @RequestParam String method,
            @RequestParam String paymentId) {

        try {
            PaymentService.PaymentMethod paymentMethod = PaymentService.PaymentMethod.valueOf(method);
            Map<String, Object> status = paymentService.checkPaymentStatus(paymentMethod, paymentId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "status", status
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Méthode de paiement non valide"
            ));
        }
    }

    /**
     * Simuler un paiement réussi (pour les tests)
     */
    @PostMapping("/simulate/success")
    public ResponseEntity<?> simulatePaymentSuccess(@RequestBody Map<String, Object> request) {
        Long orderId = Long.valueOf(request.get("orderId").toString());

        try {
            commandeService.mettreAJourStatutCommande(orderId,
                    com.vente_en_ligne.plateforme_e_commerce.entity.StatutCommande.CONFIRMEE);

            Commande commande = commandeService.obtenirCommandeParId(orderId);
            emailService.envoyerNotificationCommandeAuProprietaire(commande);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Paiement simulé avec succès"
            ));

        } catch (Exception e) {
            log.error("Erreur simulation paiement: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Erreur lors de la simulation"
            ));
        }
    }

    /**
     * Simuler un paiement échoué (pour les tests)
     */
    @PostMapping("/simulate/failure")
    public ResponseEntity<?> simulatePaymentFailure(@RequestBody Map<String, Object> request) {
        Long orderId = Long.valueOf(request.get("orderId").toString());

        commandeService.mettreAJourStatutCommande(orderId,
                com.vente_en_ligne.plateforme_e_commerce.entity.StatutCommande.ANNULEE);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Paiement échoué simulé"
        ));
    }

    // Exemple : endpoint POST pour signaler un paiement réussi
    @PostMapping("/notify")
    public String notifyPayment(
            @RequestParam String phoneNumber,
            @RequestParam double amount
    ) {
        // Test SMS direct
        String clientName = "Saliou";
        double montant = 1500.0;

// Message clair et moderne
        String message = String.format(
                "Bonjour %s, votre paiement de %.0f FCFA a été effectué avec succès. Merci pour votre confiance !",
                clientName,
                montant
        );

// Envoi du SMS
        smsService.sendPaymentNotification("+221711423982", message);
        //String message = "Votre paiement de " + amount + " a été effectué avec succès. Merci !";

        boolean sent = smsService.sendPaymentNotification(phoneNumber, message);

        if (sent) {
            return "Notification SMS envoyée avec succès.";
        } else {
            return "Échec de l'envoi de la notification SMS.";
        }
    }

}
