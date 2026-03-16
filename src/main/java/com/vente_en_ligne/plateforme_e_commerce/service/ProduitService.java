package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    /** Retourne tous les produits */
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    /** Retourne un produit par son ID */
    public Produit findById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id : " + id));
    }

    /** Sauvegarder un produit */
    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }

    /** Mettre à jour un produit */
    public Produit update(Long id, Produit produit) {
        Produit existing = findById(id);
        existing.setName(produit.getName());
        existing.setDescription(produit.getDescription());
        existing.setPrix(produit.getPrix());
        existing.setStock(produit.getStock());
        existing.setActif(produit.getActif());
        existing.setCategorie(produit.getCategorie());
        existing.setImageUrl(produit.getImageUrl());
        return produitRepository.save(existing);
    }

    /** Supprimer un produit */
    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> findByCategorie(Long categorieId) {
        return produitRepository.findByCategorieId(categorieId);
    }
    public List<Produit> findNouveauxProduits() {
        return produitRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
