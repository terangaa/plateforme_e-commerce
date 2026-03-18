package com.vente_en_ligne.plateforme_e_commerce.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vente_en_ligne.plateforme_e_commerce.service.CategorieService;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import com.vente_en_ligne.plateforme_e_commerce.service.EmailService;
import com.vente_en_ligne.plateforme_e_commerce.service.ProduitService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebController {

    private final ProduitService produitService;
    private final CategorieService categorieService;
    private final CommandeService commandeService;
    private final EmailService emailService;

    @GetMapping("/accueil")
    public ResponseEntity<?> accueil() {
        return ResponseEntity.ok(java.util.Map.of(
                "categories", categorieService.obtenirToutesLesCategories(),
                "produits", produitService.findAll()
        ));
    }

    @GetMapping("/produits")
    public ResponseEntity<?> getProduits() {
        List<Map<String, Object>> produits = produitService.findAll().stream()
                .map(p -> {
                    Map<String, Object> produitMap = new java.util.HashMap<>();
                    produitMap.put("id", p.getId());
                    produitMap.put("nom", p.getNom());
                    produitMap.put("description", p.getDescription());
                    produitMap.put("prix", p.getPrix());
                    produitMap.put("stock", p.getStock());
                    produitMap.put("imageUrl", p.getImageUrl());
                    produitMap.put("actif", p.getActif());
                    if (p.getCategorie() != null) {
                        produitMap.put("categorie", p.getCategorie().getName());
                    } else {
                        produitMap.put("categorie", null);
                    }
                    return produitMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(produits);
    }

    @PostMapping("/contact")
    public ResponseEntity<?> soumettreContact(@RequestBody Map<String, String> contactData) {
        try {
            String nom = contactData.get("name");
            String email = contactData.get("email");
            String sujet = contactData.get("subject");
            String message = contactData.get("message");

            // Journaliser les données reçues
            System.out.println("=== Données de contact reçues ===");
            System.out.println("Nom: " + nom);
            System.out.println("Email: " + email);
            System.out.println("Sujet: " + sujet);
            System.out.println("Message: " + message);

            // Envoyer l'email
            emailService.envoyerEmailContact(nom, email, sujet, message);
            System.out.println("Email envoyé avec succès à ceesaysamba24@gmail.com");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Message envoyé avec succès"
            ));
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email de contact: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Erreur lors de l'envoi du message"
            ));
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Erreur inattendue: " + e.getMessage()
            ));
        }
    }

//    @PostMapping("/commande")
//    public ResponseEntity<?> creerCommande(@RequestBody RequeteCommandeDTO requeteCommande) {
//        Commande commande = commandeService.creerCommande(requeteCommande);
//        return ResponseEntity.ok(commande);
//    }
}
