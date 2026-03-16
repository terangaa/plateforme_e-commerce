package com.vente_en_ligne.plateforme_e_commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Pages web Thymeleaf
@Controller
@RequestMapping("/views")
public class ViewController {

    @GetMapping("/produits")
    public String products() {
        return "products"; // renvoie products.html
    }
}