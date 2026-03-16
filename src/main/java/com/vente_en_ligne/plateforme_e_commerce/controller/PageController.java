package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final CommandeService commandeService;

    @GetMapping("/confirmation")
    public String confirmation(@RequestParam Long id, Model model) {
        Commande commande = commandeService.getCommandeById(id);

        if (commande == null) {
            return "error"; // page error.html à créer
        }

        model.addAttribute("commande", commande);
        model.addAttribute("total", commande.getTotal());
        return "confirmation"; // templates/confirmation.html
    }

    @GetMapping("/confirmation-commandes")
    public String adminCommandes(Model model) {
        model.addAttribute("commandes",
                commandeService.obtenirToutesLesCommandes());
        return "confirmation-commandes";
    }
}
