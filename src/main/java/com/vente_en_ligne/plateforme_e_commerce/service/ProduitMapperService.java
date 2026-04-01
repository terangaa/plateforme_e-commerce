package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.dto.*;
import com.vente_en_ligne.plateforme_e_commerce.entity.*;
import com.vente_en_ligne.plateforme_e_commerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProduitMapperService {

    private final CategorieRepository categorieRepository;
    private final UserRepository userRepository;

    /**
     * DTO -> Entity (CREATE)
     */
    public Produit fromDTO(ProduitRequestDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("ProduitRequestDTO ne doit pas être null");
        }

        Produit produit = new Produit();

        // Champs simples
        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setPrix(dto.getPrix());
        produit.setStock(dto.getStock());
        produit.setQuantite(dto.getQuantite());
        produit.setImageUrl(dto.getImageUrl());
        produit.setActive(dto.getActif());

        // ✅ Catégorie (obligatoire)
        if (dto.getCategorieId() == null) {
            throw new RuntimeException("categorieId est obligatoire");
        }

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Categorie introuvable avec id = " + dto.getCategorieId()));

        produit.setCategorie(categorie);

        // ✅ Propriétaire (obligatoire)
        if (dto.getProprietaireId() == null) {
            throw new RuntimeException("proprietaireId est obligatoire");
        }

        User user = userRepository.findById(dto.getProprietaireId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec id = " + dto.getProprietaireId()));

        produit.setProprietaire(user);

        return produit;
    }

    /**
     * UPDATE partiel (meilleure pratique 🔥)
     */
    public void updateEntity(Produit produit, ProduitRequestDTO dto) {

        if (produit == null || dto == null) {
            throw new IllegalArgumentException("Produit ou DTO null");
        }

        // Champs simples
        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setPrix(dto.getPrix());
        produit.setStock(dto.getStock());
        produit.setQuantite(dto.getQuantite());
        produit.setImageUrl(dto.getImageUrl());
        produit.setActive(dto.getActif());

        // Catégorie
        if (dto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                    .orElseThrow(() -> new RuntimeException("Categorie introuvable avec id = " + dto.getCategorieId()));
            produit.setCategorie(categorie);
        }

        // Propriétaire
        if (dto.getProprietaireId() != null) {
            User user = userRepository.findById(dto.getProprietaireId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec id = " + dto.getProprietaireId()));
            produit.setProprietaire(user);
        }
    }

    /**
     * Entity -> DTO (RESPONSE)
     */
    public ProduitResponseDTO toDTO(Produit produit) {

        if (produit == null) {
            return null;
        }

        ProduitResponseDTO dto = new ProduitResponseDTO();

        // Champs simples
        dto.setId(produit.getId());
        dto.setNom(produit.getNom());
        dto.setDescription(produit.getDescription());
        dto.setPrix(produit.getPrix());
        dto.setStock(produit.getStock());
        dto.setQuantite(produit.getQuantite());
        dto.setImageUrl(produit.getImageUrl());
        dto.setActif(produit.getActive());
        dto.setCreatedAt(produit.getCreatedAt());

        // ✅ Catégorie (safe)
        if (produit.getCategorie() != null) {
            CategorieResponseDTO catDTO = new CategorieResponseDTO();
            catDTO.setId(produit.getCategorie().getId());
            catDTO.setName(produit.getCategorie().getNom());
            catDTO.setDescription(produit.getCategorie().getDescription());
            catDTO.setImageUrl(produit.getCategorie().getImageUrl());
            dto.setCategorie(catDTO);
        }

        // ✅ Propriétaire (safe 🔥)
        if (produit.getProprietaire() != null) {
            UserResponseDTO userDTO = new UserResponseDTO();
            userDTO.setId(produit.getProprietaire().getId());
            userDTO.setNom(produit.getProprietaire().getNom());
            userDTO.setPrenom(produit.getProprietaire().getPrenom());
            userDTO.setNomComplet(produit.getProprietaire().getNomComplet());
            userDTO.setEmail(produit.getProprietaire().getEmail());
            dto.setProprietaire(userDTO);
        }

        return dto;
    }
}