package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.entity.ElementCommande;
import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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

        helper.setTo("sadikhyade851@gmail.com");
       // helper.setTo("ceesaysamba24@gmail.com");
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
            contenu.append("- Produit: ").append(p.getName())
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
            contenu.append("- Produit: ").append(p.getName())
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
}