package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/catalogue")
    public List<Produit> catalogue() {
        return clientService.consulterCatalogue();
    }

    @GetMapping("/produits/{id}")
    public Produit voirProduit(@PathVariable Long id) {
        return clientService.voirProduit(id);
    }
}