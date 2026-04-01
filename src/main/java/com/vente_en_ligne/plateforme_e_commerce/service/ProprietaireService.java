package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.entity.User;
import com.vente_en_ligne.plateforme_e_commerce.repository.ProduitRepository;
import com.vente_en_ligne.plateforme_e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProprietaireService {

    private final ProduitRepository produitRepository;
    private final UserRepository userRepository;

    // 🔹 Récupérer les produits du propriétaire connecté
    public List<Produit> getMesProduits(String email) {
        User proprietaire = getUserByEmail(email);
        return produitRepository.findByProprietaire(proprietaire);
    }

    // 🔹 Récupérer tous les produits
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // 🔹 Ajouter un produit pour un propriétaire
    @Transactional
    public Produit addProduit(Produit produit, String email) {
        User proprietaire = getUserByEmail(email);
        produit.setProprietaire(proprietaire);
        return produitRepository.save(produit);
    }

    // 🔹 Mettre à jour un produit du propriétaire
    @Transactional
    public Produit updateProduit(Produit produit, String email) {
        User proprietaire = getUserByEmail(email);

        Produit existingProduit = produitRepository.findById(produit.getId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (!existingProduit.getProprietaire().getId().equals(proprietaire.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier ce produit");
        }

        existingProduit.setNom(produit.getNom());
        existingProduit.setDescription(produit.getDescription());
        existingProduit.setPrix(produit.getPrix());
        existingProduit.setStock(produit.getStock());
        existingProduit.setImageUrl(produit.getImageUrl());
        existingProduit.setActive(produit.getActive());
        existingProduit.setCategorie(produit.getCategorie());

        return produitRepository.save(existingProduit);
    }

    // 🔹 Supprimer un produit du propriétaire
    @Transactional
    public void deleteProduit(Long id, String email) {
        User proprietaire = getUserByEmail(email);
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (!produit.getProprietaire().getId().equals(proprietaire.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer ce produit");
        }

        produitRepository.delete(produit);
    }

    // 🔹 Récupérer un utilisateur par email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}