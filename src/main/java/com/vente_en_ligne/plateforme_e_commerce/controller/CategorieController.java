package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.entity.Categorie;
import com.vente_en_ligne.plateforme_e_commerce.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    /** Page avec toutes les catégories */
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categorieService.findAll());
        return "categories"; // Thymeleaf template categories.html
    }

    /** Page des produits d’une catégorie spécifique */
    @GetMapping("/{id}/produits")
    public String produitsParCategorie(@PathVariable Long id, Model model) {
        Categorie categorie = categorieService.findById(id);
        model.addAttribute("categorieSelectionnee", categorie);
        model.addAttribute("produits", categorie.getProduits());
        return "products"; // même template produits que pour tous les produits
    }
}