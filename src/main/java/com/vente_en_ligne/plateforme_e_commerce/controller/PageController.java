package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final CommandeService commandeService;

    // Pages principales
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index")
    public String indexAlt() {
        return "index";
    }

    // Pages de produits (API REST donc pas de vue Thymeleaf ici)
    // Les routes products sont gérées par ProduitController
    // Pages de panier
    @GetMapping("/cart")
    public String cart() {
        return "panier";
    }

    @GetMapping("/panier")
    public String panier() {
        return "panier";
    }

    // Page de paiement
    @GetMapping("/payment")
    public String payment(@RequestParam(required = false) Long orderId, Model model) {
        if (orderId != null) {
            model.addAttribute("orderId", orderId);
            Commande commande = commandeService.getCommandeById(orderId);
            if (commande != null) {
                model.addAttribute("total", commande.getTotal());
            }
        }
        return "payment";
    }

    @GetMapping("/paiement")
    public String paiement(@RequestParam(required = false) Long orderId, Model model) {
        return payment(orderId, model);
    }

    // Page détail produit
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("productId", id);
        return "product-detail";
    }

    @GetMapping("/produit/{id}")
    public String produitDetail(@PathVariable Long id, Model model) {
        return productDetail(id, model);
    }

    // Confirmation de commande
    @GetMapping("/order-confirmation")
    public String orderConfirmation(@RequestParam(required = false) String orderId, Model model) {
        if (orderId != null) {
            try {
                Long id = Long.parseLong(orderId);
                Commande commande = commandeService.getCommandeById(id);
                if (commande != null) {
                    model.addAttribute("commande", commande);
                    model.addAttribute("total", commande.getTotal());
                }
            } catch (NumberFormatException e) {
                // Ignore parse errors
            }
        }
        return "order-confirmation";
    }

    @GetMapping("/confirmation")
    public String confirmation(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Commande commande = commandeService.getCommandeById(id);
            if (commande != null) {
                model.addAttribute("commande", commande);
                model.addAttribute("total", commande.getTotal());
            }
        }
        return "order-confirmation";
    }

    @GetMapping("/confirmation-commande")
    public String confirmationCommande(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Commande commande = commandeService.getCommandeById(id);
            if (commande != null) {
                model.addAttribute("commande", commande);
                model.addAttribute("total", commande.getTotal());
            }
        }
        return "order-confirmation";
    }

    // Pages d'authentification
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/inscription")
    public String inscription() {
        return "register";
    }

    // Page de contact
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
