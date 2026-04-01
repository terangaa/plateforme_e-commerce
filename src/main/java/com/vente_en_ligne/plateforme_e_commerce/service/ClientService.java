package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ProduitRepository produitRepository;

    public List<Produit> consulterCatalogue() {
        return produitRepository.findAll();
    }

    public Produit voirProduit(Long id) {
        return produitRepository.findById(id).orElse(null);
    }
}