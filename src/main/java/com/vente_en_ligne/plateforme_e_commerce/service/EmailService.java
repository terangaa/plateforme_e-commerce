package com.vente_en_ligne.plateforme_e_commerce.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.entity.ElementCommande;
import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Envoie un email de notification au propriétaire du site
     */
    public void envoyerNotificationCommandeAuProprietaire(Commande commande) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo("ceesaysamba24@gmail.com");
        helper.setSubject("Nouvelle commande reçue : #" + commande.getId());

        StringBuilder contenu = new StringBuilder();
        contenu.append("Une nouvelle commande a été passée.\n\n");
        contenu.append("Client: ").append(commande.getNomClient()).append("\n");
        contenu.append("Email: ").append(commande.getEmailClient()).append("\n");
        contenu.append("Téléphone: ").append(commande.getTelephoneClient()).append("\n");
        contenu.append("Adresse: ").append(commande.getAdresseClient()).append("\n\n");
        contenu.append("Détails de la commande:\n");

        for (ElementCommande el : commande.getElements()) {
            Produit p = el.getProduit(); // <- getter correct
            contenu.append("- Produit: ").append(p.getNom())
                    .append(", Quantité: ").append(el.getQuantite())
                    .append(", Prix Unitaire: ").append(el.getPrixUnitaire())
                    .append(", Sous-total: ").append(el.getSousTotal())
                    .append("\n");
        }

        contenu.append("\nTotal commande: ").append(commande.getTotal());

        helper.setText(contenu.toString());

        mailSender.send(message);
    }

    /**
     * Envoie un email de confirmation au client
     */
    public void envoyerConfirmationCommandeAuClient(Commande commande) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(commande.getEmailClient());
        helper.setSubject("Confirmation de votre commande #" + commande.getId());

        StringBuilder contenu = new StringBuilder();
        contenu.append("Bonjour ").append(commande.getNomClient()).append(",\n\n");
        contenu.append("Merci pour votre commande. Voici le récapitulatif:\n\n");

        for (ElementCommande el : commande.getElements()) {
            Produit p = el.getProduit();
            contenu.append("- Produit: ").append(p.getNom())
                    .append(", Quantité: ").append(el.getQuantite())
                    .append(", Prix Unitaire: ").append(el.getPrixUnitaire())
                    .append(", Sous-total: ").append(el.getSousTotal())
                    .append("\n");
        }

        contenu.append("\nTotal: ").append(commande.getTotal());
        contenu.append("\n\nNous vous remercions de votre confiance.");

        helper.setText(contenu.toString());

        mailSender.send(message);
    }

    /**
     * Envoie un email de contact au propriétaire du site
     */
    public void envoyerEmailContact(String nom, String email, String sujet, String message) throws MessagingException {
        System.out.println("=== Début envoi email de contact ===");
        System.out.println("De: " + email);
        System.out.println("Nom: " + nom);
        System.out.println("Sujet: " + sujet);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo("ceesaysamba24@gmail.com");
        helper.setSubject("Nouveau message de contact: " + sujet);
        helper.setReplyTo(email); // Permet de répondre directement à l'expéditeur

        StringBuilder contenu = new StringBuilder();
        contenu.append("Nouveau message de formulaire de contact\n\n");
        contenu.append("Nom: ").append(nom).append("\n");
        contenu.append("Email: ").append(email).append("\n");
        contenu.append("Sujet: ").append(sujet).append("\n\n");
        contenu.append("Message:\n").append(message);

        helper.setText(contenu.toString());

        System.out.println("Envoi de l'email...");
        mailSender.send(mimeMessage);
        System.out.println("Email de contact envoyé avec succès à ceesaysamba24@gmail.com");
    }
}
