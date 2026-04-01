package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.entity.User;
import com.vente_en_ligne.plateforme_e_commerce.service.ProprietaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proprietaire")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROPRIETAIRE')") // Seul un propriétaire peut accéder
public class ProprietaireController {

    private final ProprietaireService proprietaireService;

    // 🔹 Produits du propriétaire connecté
    @GetMapping("/mes-produits")
    public List<Produit> listMesProduits(Authentication authentication) {
        String email = authentication.getName();
        return proprietaireService.getMesProduits(email);
    }

    // 🔹 Tous les produits
    @GetMapping("/produits")
    public List<Produit> listAllProduits() {
        return proprietaireService.getAllProduits();
    }

    // 🔹 Ajouter un produit
    @PostMapping("/produits")
    public Produit addProduit(@RequestBody Produit produit, Authentication authentication) {
        String email = authentication.getName();
        return proprietaireService.addProduit(produit, email);
    }

    // 🔹 Mettre à jour un produit
    @PutMapping("/produits")
    public Produit updateProduit(@RequestBody Produit produit, Authentication authentication) {
        String email = authentication.getName();
        return proprietaireService.updateProduit(produit, email);
    }

    // 🔹 Supprimer un produit
    @DeleteMapping("/produits/{id}")
    public void deleteProduit(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        proprietaireService.deleteProduit(id, email);
    }
}