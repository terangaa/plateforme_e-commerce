package com.vente_en_ligne.plateforme_e_commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanierViewController {

    @GetMapping("/panier")
    public String viewCart() {
        return "panier"; // Thymeleaf va chercher panier.html dans templates
    }
}