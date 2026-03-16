package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.service.CategorieService;
import com.vente_en_ligne.plateforme_e_commerce.service.ProduitService;
import com.vente_en_ligne.plateforme_e_commerce.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;
    private final CategorieService categorieService;
    private final StorageService storageService;

    /** Liste tous les produits */
    @GetMapping
    public String listProduits(Model model) {
        model.addAttribute("produits", produitService.findAll());
        model.addAttribute("produit", new Produit()); // <-- nécessaire pour le formulaire
        model.addAttribute("categories", categorieService.findAll()); // <-- nécessaire pour le select
        return "products"; // nom du template Thymeleaf
    }

    /** Formulaire pour créer un produit */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("categories", categorieService.findAll());
        return "create-product";
    }

    @GetMapping("/nouveau")
    public String showFormProduit(Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("categories", categorieService.getAllCategories());
        return "produit-form"; // nom du fichier HTML dans resources/templates/
    }

    @GetMapping("/nouveautes")
    public String listNouveauxProduits(Model model) {
        // récupère les 10 derniers produits ajoutés, triés par date décroissante
        model.addAttribute("produits", produitService.findNouveauxProduits());
        model.addAttribute("categories", categorieService.findAll());
        return "products"; // réutilise le même template
    }

    /** Sauvegarder un produit depuis le formulaire */
    @PostMapping("/save")
    public String saveProduit(@ModelAttribute Produit produit,
                              @RequestParam("image") MultipartFile image) {

        if (!image.isEmpty()) {
            String imageUrl = storageService.storeFile(image); // sauvegarde image
            produit.setImageUrl(imageUrl);
        }

        produitService.save(produit);
        return "redirect:/produits";
    }

    /** Formulaire pour modifier un produit */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Produit produit = produitService.findById(id);
        model.addAttribute("produit", produit);
        model.addAttribute("categories", categorieService.findAll());
        return "edit-product";
    }

    /** Liste les produits par catégorie */
    @GetMapping("/categorie/{categorieId}")
    public String listProduitsParCategorie(@PathVariable Long categorieId, Model model) {
        // Récupérer les produits de cette catégorie
        model.addAttribute("produits", produitService.findByCategorie(categorieId));

        // Récupérer la catégorie sélectionnée pour affichage
        model.addAttribute("categorieSelectionnee", categorieService.findById(categorieId));

        // Pour le menu ou le formulaire
        model.addAttribute("categories", categorieService.findAll());

        return "products"; // même template que listProduits
    }

    /** Mettre à jour un produit depuis le formulaire */
    @PostMapping("/update/{id}")
    public String updateProduit(@PathVariable Long id,
                                @ModelAttribute Produit produit,
                                @RequestParam("image") MultipartFile image) {

        if (!image.isEmpty()) {
            String imageUrl = storageService.storeFile(image);
            produit.setImageUrl(imageUrl);
        }

        produitService.update(id, produit);
        return "redirect:/produits";
    }

    /** Supprimer un produit */
    @GetMapping("/delete/{id}")
    public String deleteProduit(@PathVariable Long id) {
        produitService.delete(id);
        return "redirect:/produits";
    }
}
