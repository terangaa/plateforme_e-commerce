package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.dto.RequeteCommandeDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.service.CategorieService;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import com.vente_en_ligne.plateforme_e_commerce.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebController {

    private final ProduitService produitService;
    private final CategorieService categorieService;
    private final CommandeService commandeService;

    @GetMapping("/accueil")
    public ResponseEntity<?> accueil() {
        return ResponseEntity.ok(java.util.Map.of(
                "categories", categorieService.obtenirToutesLesCategories(),
                "produits", produitService.findAll()
        ));
    }

//    @PostMapping("/commande")
//    public ResponseEntity<?> creerCommande(@RequestBody RequeteCommandeDTO requeteCommande) {
//        Commande commande = commandeService.creerCommande(requeteCommande);
//        return ResponseEntity.ok(commande);
//    }
}