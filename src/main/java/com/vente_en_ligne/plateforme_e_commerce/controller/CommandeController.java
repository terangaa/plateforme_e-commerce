package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.dto.RequeteCommandeDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.entity.StatutCommande;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import com.vente_en_ligne.plateforme_e_commerce.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST pour toutes les opérations liées aux commandes
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;
    private final EmailService emailService;

    // Obtenir toutes les commandes
    @GetMapping("/commandes")
    public ResponseEntity<List<Commande>> obtenirToutesLesCommandes() {
        return ResponseEntity.ok(commandeService.obtenirToutesLesCommandes());
    }

    // Obtenir commande par ID
    @GetMapping("/commande/{id}")
    public ResponseEntity<Commande> obtenirCommandeParId(@PathVariable Long id) {
        Commande commande = commandeService.obtenirCommandeParId(id);
        if (commande == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commande);
    }

    // Créer une nouvelle commande
    @PostMapping("/commande")
    public ResponseEntity<?> creerCommande(@Valid @RequestBody RequeteCommandeDTO req) {
        Commande cree;

        try {
            cree = commandeService.creerCommande(req);  // capture MessagingException
        } catch (MessagingException e) {
            // Logue l'erreur mais continue
            System.err.println("Erreur création commande (email) : " + e.getMessage());
            // On peut quand même créer un objet vide pour éviter null
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Impossible de créer la commande à cause de l'email"
            ));
        }

        // Emails non bloquants
        try {
            emailService.envoyerNotificationCommandeAuProprietaire(cree);
        } catch (Exception e) {
            System.err.println("Erreur email (non bloquant) : " + e.getMessage());
        }

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("orderId", cree.getId());

        return ResponseEntity.ok(res);
    }

    // Mettre à jour le statut d’une commande
    @PatchMapping("/commandes/{id}/statut")
    public ResponseEntity<Map<String, Object>> mettreAJourStatutCommande(
            @PathVariable Long id,
            @RequestParam StatutCommande statut) {

        Map<String, Object> response = new HashMap<>();

        try {
            Commande commande = commandeService.mettreAJourStatutCommande(id, statut);

            // EMAIL CLIENT APRES VALIDATION ADMIN
            if (statut == StatutCommande.CONFIRMEE) {
                emailService.envoyerConfirmationCommandeAuClient(commande);
            }

            response.put("success", true);
            response.put("order", commande);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}