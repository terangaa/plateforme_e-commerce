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
public class AdminService {

    private final UserRepository userRepository;
    private final ProduitRepository produitRepository;

    // ─── USERS ───
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        User existing = getUserById(user.getId());
        existing.setNom(user.getNom());
        existing.setPrenom(user.getPrenom());
        existing.setEmail(user.getEmail());
        existing.setTelephone(user.getTelephone());
        existing.setAdresse(user.getAdresse());
        existing.setVille(user.getVille());
        existing.setCodePostal(user.getCodePostal());
        existing.setPays(user.getPays());
        existing.setRole(user.getRole());
        existing.setActif(user.getActif());
        // Only update password if provided (not null and not empty)
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(user.getPassword());
        }
        return userRepository.save(existing);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ─── PRODUITS ───
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
    }

    @Transactional
    public Produit addProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    @Transactional
    public Produit updateProduit(Produit produit) {
        Produit existing = getProduitById(produit.getId());
        existing.setNom(produit.getNom());
        existing.setDescription(produit.getDescription());
        existing.setPrix(produit.getPrix());
        existing.setQuantite(produit.getQuantite());
        existing.setProprietaire(produit.getProprietaire());
        return produitRepository.save(existing);
    }

    @Transactional
    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }
}