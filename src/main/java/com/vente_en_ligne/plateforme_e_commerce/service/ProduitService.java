package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    // ─── Récupérer tous les produits ───
    public List<Produit> getAllProduits() {
        return produitRepository.findAll(); // retourne tous les produits
    }

    // ─── Récupérer un produit par ID ───

    // ─── Ajouter / sauvegarder un produit ───
    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }

    // ─── Mettre à jour un produit ───
    public Produit update(Produit produit) {
        // Vérifie si le produit existe
        if (produit.getId() != null && produitRepository.existsById(produit.getId())) {
            return produitRepository.save(produit);
        }
        throw new RuntimeException("Produit non trouvé avec l'ID : " + produit.getId());
    }

    // ─── Supprimer un produit ───
    public void softDeleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
        produit.setActive(false);  // marque le produit comme inactif
        produitRepository.save(produit);
    }

    public String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null; // aucun fichier fourni
        }

        try {
            // 1️⃣ Créer le dossier uploads/images s'il n'existe pas
            String uploadDir = "uploads/images/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2️⃣ Générer un nom unique
            String originalFilename = imageFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // 3️⃣ Sauvegarder le fichier
            Path filePath = Paths.get(uploadDir + filename);
            Files.write(filePath, imageFile.getBytes());

            // 4️⃣ Retourner l'URL relative pour Thymeleaf
            return "/" + uploadDir + filename;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
        }
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
    }

    public Optional<Produit> getProduitByIdOptional(Long id) {
        return produitRepository.findById(id); // retourne Optional<Produit>
    }
}