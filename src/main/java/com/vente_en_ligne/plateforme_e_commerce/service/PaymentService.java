package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service de paiement pour Wave, Orange Money et Free Money Ce service gère
 * les三种 méthodes de paiement mobiles utilisées au Sénégal et en Gambie
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    // Configuration des marchands (à configurer avec vos identifiants réels)
    private static final String WAVE_MERCHANT_ID = "SHOPLINE001";
    private static final String ORANGE_MERCHANT_ID = "SHOPLINE001";
    private static final String FREE_MERCHANT_ID = "SHOPLINE001";

    // URLs des API de paiement (à remplacer par les URLs réelles en production)
    private static final String WAVE_API_URL = "https://api.wave.com/payment";
    private static final String ORANGE_API_URL = "https://api.orange.com/payment";
    private static final String FREE_API_URL = "https://api.free.com/payment";

    public enum PaymentMethod {
        WAVE("Wave", "Paiement mobile Wave (Sénégal/Gambie)"),
        ORANGE_MONEY("Orange Money", "Paiement mobile Orange Money"),
        FREE_MONEY("Free Money", "Paiement mobile Free Money"),
        CASH_ON_DELIVERY("Cash on Delivery", "Paiement à la livraison");

        private final String displayName;
        private final String description;

        PaymentMethod(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum PaymentStatus {
        PENDING("En attente"),
        PROCESSING("En cours de traitement"),
        COMPLETED("Terminé"),
        FAILED("Échoué"),
        CANCELLED("Annulé"),
        REFUNDED("Remboursé");

        private final String displayName;

        PaymentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Crée une demande de paiement pour Wave
     */
    public Map<String, Object> createWavePayment(Commande commande, String phoneNumber) {
        log.info("Création paiement Wave pour commande {} - Montant: {} - Téléphone: {}",
                commande.getId(), commande.getTotal(), phoneNumber);

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("merchant_id", WAVE_MERCHANT_ID);
        paymentRequest.put("amount", commande.getTotal().doubleValue());
        paymentRequest.put("currency", "XOF");
        paymentRequest.put("order_id", commande.getId());
        paymentRequest.put("customer_phone", phoneNumber);
        paymentRequest.put("customer_email", commande.getEmailClient());
        paymentRequest.put("payment_method", "WAVE");
        paymentRequest.put("callback_url", "/api/payment/wave/callback");
        paymentRequest.put("return_url", "/payment/success?order=" + commande.getId());
        paymentRequest.put("cancel_url", "/payment/cancel?order=" + commande.getId());

        // Simulation de la création de payment - en production, appeler l'API Wave
        String waveCheckoutUrl = WAVE_API_URL + "/checkout/" + UUID.randomUUID().toString();
        paymentRequest.put("checkout_url", waveCheckoutUrl);
        paymentRequest.put("status", PaymentStatus.PENDING.name());
        paymentRequest.put("created_at", java.time.Instant.now().toString());

        log.info("Paiement Wave créé avec succès - URL: {}", waveCheckoutUrl);

        return paymentRequest;
    }

    /**
     * Crée une demande de paiement pour Orange Money
     */
    public Map<String, Object> createOrangeMoneyPayment(Commande commande, String phoneNumber) {
        log.info("Création paiement Orange Money pour commande {} - Montant: {} - Téléphone: {}",
                commande.getId(), commande.getTotal(), phoneNumber);

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("merchant_id", ORANGE_MERCHANT_ID);
        paymentRequest.put("amount", commande.getTotal().doubleValue());
        paymentRequest.put("currency", "XOF");
        paymentRequest.put("order_id", commande.getId());
        paymentRequest.put("subscriber_phone", phoneNumber);
        paymentRequest.put("customer_email", commande.getEmailClient());
        paymentRequest.put("payment_method", "ORANGE_MONEY");
        paymentRequest.put("callback_url", "/api/payment/orange/callback");
        paymentRequest.put("return_url", "/payment/success?order=" + commande.getId());
        paymentRequest.put("cancel_url", "/payment/cancel?order=" + commande.getId());

        // En production, appeler l'API Orange Money
        String orangeCheckoutUrl = ORANGE_API_URL + "/checkout/" + UUID.randomUUID().toString();
        paymentRequest.put("checkout_url", orangeCheckoutUrl);
        paymentRequest.put("status", PaymentStatus.PENDING.name());
        paymentRequest.put("created_at", java.time.Instant.now().toString());

        log.info("Paiement Orange Money créé avec succès - URL: {}", orangeCheckoutUrl);

        return paymentRequest;
    }

    /**
     * Crée une demande de paiement pour Free Money
     */
    public Map<String, Object> createFreeMoneyPayment(Commande commande, String phoneNumber) {
        log.info("Création paiement Free Money pour commande {} - Montant: {} - Téléphone: {}",
                commande.getId(), commande.getTotal(), phoneNumber);

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("merchant_id", FREE_MERCHANT_ID);
        paymentRequest.put("amount", commande.getTotal().doubleValue());
        paymentRequest.put("currency", "XOF");
        paymentRequest.put("order_id", commande.getId());
        paymentRequest.put("customer_phone", phoneNumber);
        paymentRequest.put("customer_email", commande.getEmailClient());
        paymentRequest.put("payment_method", "FREE_MONEY");
        paymentRequest.put("callback_url", "/api/payment/free/callback");
        paymentRequest.put("return_url", "/payment/success?order=" + commande.getId());
        paymentRequest.put("cancel_url", "/payment/cancel?order=" + commande.getId());

        // En production, appeler l'API Free Money
        String freeCheckoutUrl = FREE_API_URL + "/checkout/" + UUID.randomUUID().toString();
        paymentRequest.put("checkout_url", freeCheckoutUrl);
        paymentRequest.put("status", PaymentStatus.PENDING.name());
        paymentRequest.put("created_at", java.time.Instant.now().toString());

        log.info("Paiement Free Money créé avec succès - URL: {}", freeCheckoutUrl);

        return paymentRequest;
    }

    /**
     * Crée une commande en attente de paiement à la livraison
     */
    public Map<String, Object> createCashOnDeliveryPayment(Commande commande) {
        log.info("Création paiement paiement à la livraison pour commande {}", commande.getId());

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("order_id", commande.getId());
        paymentRequest.put("amount", commande.getTotal().doubleValue());
        paymentRequest.put("currency", "XOF");
        paymentRequest.put("payment_method", "CASH_ON_DELIVERY");
        paymentRequest.put("status", PaymentStatus.PENDING.name());
        paymentRequest.put("description", "Paiement à la livraison");
        paymentRequest.put("created_at", java.time.Instant.now().toString());

        log.info("Commande avec paiement à la livraison créée avec succès");

        return paymentRequest;
    }

    /**
     * Vérifie le statut d'un paiement Wave
     */
    public Map<String, Object> checkWavePaymentStatus(String paymentId) {
        log.info("Vérification statut paiement Wave: {}", paymentId);

        // En production, appeler l'API Wave pour vérifier le statut
        Map<String, Object> status = new HashMap<>();
        status.put("payment_id", paymentId);
        status.put("status", PaymentStatus.COMPLETED.name());
        status.put("checked_at", java.time.Instant.now().toString());

        return status;
    }

    /**
     * Vérifie le statut d'un paiement Orange Money
     */
    public Map<String, Object> checkOrangeMoneyPaymentStatus(String paymentId) {
        log.info("Vérification statut paiement Orange Money: {}", paymentId);

        // En production, appeler l'API Orange pour vérifier le statut
        Map<String, Object> status = new HashMap<>();
        status.put("payment_id", paymentId);
        status.put("status", PaymentStatus.COMPLETED.name());
        status.put("checked_at", java.time.Instant.now().toString());

        return status;
    }

    /**
     * Vérifie le statut d'un paiement Free Money
     */
    public Map<String, Object> checkFreeMoneyPaymentStatus(String paymentId) {
        log.info("Vérification statut paiement Free Money: {}", paymentId);

        // En production, appeler l'API Free pour vérifier le statut
        Map<String, Object> status = new HashMap<>();
        status.put("payment_id", paymentId);
        status.put("status", PaymentStatus.COMPLETED.name());
        status.put("checked_at", java.time.Instant.now().toString());

        return status;
    }

    /**
     * Traite le callback de paiement Wave
     */
    public boolean processWaveCallback(Map<String, Object> callbackData) {
        log.info("Traitement callback Wave: {}", callbackData);

        String status = (String) callbackData.get("status");
        String paymentId = (String) callbackData.get("payment_id");

        // Vérifier la signature du callback en production
        if ("SUCCESS".equals(status)) {
            log.info("Paiement Wave confirmé: {}", paymentId);
            return true;
        }

        return false;
    }

    /**
     * Traite le callback de paiement Orange Money
     */
    public boolean processOrangeCallback(Map<String, Object> callbackData) {
        log.info("Traitement callback Orange Money: {}", callbackData);

        String status = (String) callbackData.get("status");
        String paymentId = (String) callbackData.get("payment_id");

        if ("SUCCESS".equals(status)) {
            log.info("Paiement Orange Money confirmé: {}", paymentId);
            return true;
        }

        return false;
    }

    /**
     * Traite le callback de paiement Free Money
     */
    public boolean processFreeCallback(Map<String, Object> callbackData) {
        log.info("Traitement callback Free Money: {}", callbackData);

        String status = (String) callbackData.get("status");
        String paymentId = (String) callbackData.get("payment_id");

        if ("SUCCESS".equals(status)) {
            log.info("Paiement Free Money confirmé: {}", paymentId);
            return true;
        }

        return false;
    }

    /**
     * Initie un paiement en fonction de la méthode choisie
     */
    public Map<String, Object> initiatePayment(Commande commande, PaymentMethod method, String phoneNumber) {
        switch (method) {
            case WAVE:
                return createWavePayment(commande, phoneNumber);
            case ORANGE_MONEY:
                return createOrangeMoneyPayment(commande, phoneNumber);
            case FREE_MONEY:
                return createFreeMoneyPayment(commande, phoneNumber);
            case CASH_ON_DELIVERY:
                return createCashOnDeliveryPayment(commande);
            default:
                throw new IllegalArgumentException("Méthode de paiement non supportée: " + method);
        }
    }

    /**
     * Vérifie le statut d'un paiement
     */
    public Map<String, Object> checkPaymentStatus(PaymentMethod method, String paymentId) {
        switch (method) {
            case WAVE:
                return checkWavePaymentStatus(paymentId);
            case ORANGE_MONEY:
                return checkOrangeMoneyPaymentStatus(paymentId);
            case FREE_MONEY:
                return checkFreeMoneyPaymentStatus(paymentId);
            default:
                throw new IllegalArgumentException("Méthode de paiement non supportée: " + method);
        }
    }

    /**
     * Formate le montant pour l'affichage
     */
    public String formatAmount(BigDecimal amount) {
        return String.format("%.0f XOF", amount.doubleValue());
    }

    /**
     * Valide le numéro de téléphone pour Wave
     */
    public boolean validateWavePhoneNumber(String phoneNumber) {
        // Numéro Wave au Sénégal commence par 70, 76, 77 ou 78
        return phoneNumber != null && phoneNumber.matches("^(77|78|70|76)\\d{7}$");
    }

    /**
     * Valide le numéro de téléphone pour Orange Money
     */
    public boolean validateOrangePhoneNumber(String phoneNumber) {
        // Numéro Orange au Sénégal commence par 70, 76, 77 ou 78
        return phoneNumber != null && phoneNumber.matches("^(77|78|70|76)\\d{7}$");
    }

    /**
     * Valide le numéro de téléphone pour Free Money
     */
    public boolean validateFreePhoneNumber(String phoneNumber) {
        // Numéro Free au Sénégal commence par 70, 76, 77 ou 78
        return phoneNumber != null && phoneNumber.matches("^(77|78|70|76)\\d{7}$");
    }
}
