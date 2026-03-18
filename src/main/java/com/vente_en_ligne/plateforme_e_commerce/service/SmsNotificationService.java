package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsNotificationService {

    private final String VONAGE_API_KEY = "c200ce61";        // Remplace par ta clé API Vonage
    private final String VONAGE_API_SECRET = "^e^AH7ZH"; // Remplace par ton secret
    private final String SENDER_NUMBER = "WAVE";         // Ton numéro ou nom d'expéditeur

    private final VonageClient client;

    // Constructeur : initialise le client Vonage
    public SmsNotificationService() {
        this.client = VonageClient.builder()
                .apiKey(VONAGE_API_KEY)
                .apiSecret(VONAGE_API_SECRET)
                .build();
    }

    /**
     * Envoie un SMS simple.
     *
     * @param toNumber Numéro du destinataire (ex: +22177XXXXXXX)
     * @param message  Contenu du SMS
     */
    public void sendSms(String toNumber, String message) {
        try {
            TextMessage textMessage = new TextMessage(SENDER_NUMBER, toNumber, message);
            SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);
            System.out.println(response);

            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                log.info("SMS envoyé avec succès à {}", toNumber);
            } else {
                log.error("Erreur lors de l'envoi du SMS à {} : {}", toNumber,
                        response.getMessages().get(0).getErrorText());
            }
        } catch (Exception e) {
            log.error("Exception lors de l'envoi SMS à {} : {}", toNumber, e.getMessage());
        }
    }

    /**
     * Envoie une notification de paiement.
     *
     * @param phoneNumber Numéro du destinataire
     * @param messageText Contenu du SMS
     * @return true si le SMS a été envoyé, false sinon
     */
    public boolean sendPaymentNotification(String phoneNumber, String messageText) {
        try {
            TextMessage message = new TextMessage(SENDER_NUMBER, phoneNumber, messageText);
            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
            System.out.println(response);

            return response.getMessages().get(0).getStatus() == MessageStatus.OK;
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de la notification paiement à {} : {}", phoneNumber, e.getMessage());
            return false;
        }
    }
}