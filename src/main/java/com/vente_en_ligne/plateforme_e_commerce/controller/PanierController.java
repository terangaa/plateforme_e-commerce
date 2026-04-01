package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.dto.*;
import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import com.vente_en_ligne.plateforme_e_commerce.service.PanierService;
import com.vente_en_ligne.plateforme_e_commerce.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class PanierController {

    private final PanierService panierService;
    private final CommandeService commandeService;
    private final UserService userService; // pour récupérer info client

    // ---------------- PANIER ----------------

    @GetMapping("/panier")
    public ResponseEntity<Map<String, Object>> getPanier() {
        Map<String, Object> res = new HashMap<>();
        res.put("items", panierService.getCartItems());
        res.put("total", panierService.getCartTotal());
        res.put("itemCount", panierService.getItemCount());
        res.put("success", true);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long produitId,
            @RequestParam(defaultValue = "1") Integer quantite) {

        try {
            panierService.addToCart(produitId, quantite);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Produit ajouté au panier",
                    "items", panierService.getCartItems(),
                    "total", panierService.getCartTotal()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/panier/update/{produitId}")
    public ResponseEntity<?> updateCart(@PathVariable Long produitId,
                                        @RequestParam Integer quantite) {
        try {
            panierService.updateQuantity(produitId, quantite);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "items", panierService.getCartItems(),
                    "total", panierService.getCartTotal()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/remove/{produitId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long produitId) {
        try {
            panierService.removeFromCart(produitId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "items", panierService.getCartItems(),
                    "total", panierService.getCartTotal()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        panierService.clearCart();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Panier vidé"
        ));
    }

    // ---------------- COMMANDE ----------------

    @PostMapping("/panier/commander")
    public ResponseEntity<?> commanderDepuisPanier(Authentication auth) {
        try {
            List<PanierDTO> itemsPanier = panierService.getCartItems();
            if (itemsPanier.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Le panier est vide"
                ));
            }

            // Récupérer info client
            String email = auth.getName(); // username = email
            var client = userService.getUserByEmail(email);

            // Transformer PanierDTO -> ElementCommandeDTO
            List<ElementCommandeDTO> produits = itemsPanier.stream().map(p -> {
                ElementCommandeDTO dto = new ElementCommandeDTO();
                dto.setProduitId(p.getProduitId());
                dto.setQuantite(p.getQuantite());
                return dto;
            }).toList();

            // Créer DTO de commande
            RequeteCommandeDTO req = new RequeteCommandeDTO();
            req.setClientNom(client.getNom() + " " + client.getPrenom());
            req.setClientEmail(client.getEmail());
            req.setClientTelephone(client.getTelephone());
            req.setAdresse(client.getAdresse());
            req.setProduits(produits);

            // Créer commande
            Commande commande = commandeService.creerCommande(req);

            // Vider le panier
            panierService.clearCart();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Commande passée avec succès",
                    "commandeId", commande.getId()
            ));

        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Erreur lors de l'envoi de l'email: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/commandes")
    public ResponseEntity<List<Commande>> mesCommandes(Authentication auth) {
        String email = auth.getName();
        List<Commande> commandes = commandeService.obtenirCommandesParEmailClient(email);
        return ResponseEntity.ok(commandes);
    }

}