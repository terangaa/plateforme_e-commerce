package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.dto.PanierDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.Panier;
import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.repository.PanierRepository;
import com.vente_en_ligne.plateforme_e_commerce.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion du panier avec stockage en session
 */
@Service
@SessionScope
@RequiredArgsConstructor
public class PanierService {

    private final ProduitRepository produitRepository;
    private final List<PanierDTO> cartItems = new ArrayList<>();
    private final PanierRepository panierRepository;


    /**
     * Ajouter un produit au panier
     */
    @Transactional
    public PanierDTO addToCart(Long produitId, Integer quantite) {
        if (quantite == null || quantite <= 0) {
            quantite = 1;
        }

        // Vérifier si le produit existe
        Optional<Produit> produitOpt = produitRepository.findById(produitId);
        if (produitOpt.isEmpty()) {
            throw new IllegalArgumentException("Produit non trouvé");
        }

        Produit produit = produitOpt.get();

        // Vérifier le stock
        if (produit.getStock() != null && produit.getStock() < quantite) {
            throw new IllegalArgumentException("Stock insuffisant pour ce produit");
        }

        // Vérifier si le produit est déjà dans le panier
        Optional<PanierDTO> existingItem = cartItems.stream()
                .filter(item -> item.getProduitId().equals(produitId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Mettre à jour la quantité
            PanierDTO item = existingItem.get();
            int nouvelleQuantite = item.getQuantite() + quantite;

            if (produit.getStock() != null && produit.getStock() < nouvelleQuantite) {
                throw new IllegalArgumentException("Stock insuffisant pour cette quantité");
            }

            item.setQuantite(nouvelleQuantite);
            return item;
        } else {
            // Ajouter un nouveau produit
            PanierDTO newItem = new PanierDTO(
                    produit.getId(),
                    produit.getNom(),
                    produit.getPrix(),
                    produit.getImageUrl()
            );
            newItem.setQuantite(quantite);
            cartItems.add(newItem);
            return newItem;
        }
    }

    /**
     * Modifier la quantité d'un produit dans le panier
     */
    @Transactional
    public PanierDTO updateQuantity(Long produitId, Integer quantite) {
        if (quantite == null || quantite <= 0) {
            return removeFromCart(produitId);
        }

        // Vérifier le stock
        Optional<Produit> produitOpt = produitRepository.findById(produitId);
        if (produitOpt.isPresent()) {
            Produit produit = produitOpt.get();
            if (produit.getStock() != null && produit.getStock() < quantite) {
                throw new IllegalArgumentException("Stock insuffisant pour cette quantité");
            }
        }

        Optional<PanierDTO> itemOpt = cartItems.stream()
                .filter(item -> item.getProduitId().equals(produitId))
                .findFirst();

        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Produit non trouvé dans le panier");
        }

        PanierDTO item = itemOpt.get();
        item.setQuantite(quantite);
        return item;
    }

    /**
     * Supprimer un produit du panier
     */
    @Transactional
    public PanierDTO removeFromCart(Long produitId) {
        Optional<PanierDTO> itemOpt = cartItems.stream()
                .filter(item -> item.getProduitId().equals(produitId))
                .findFirst();

        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Produit non trouvé dans le panier");
        }

        PanierDTO removedItem = itemOpt.get();
        cartItems.remove(removedItem);
        return removedItem;
    }

    /**
     * Obtenir tous les éléments du panier
     */
    public List<PanierDTO> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    /**
     * Obtenir le nombre d'éléments dans le panier
     */
    public int getItemCount() {
        return cartItems.stream()
                .mapToInt(PanierDTO::getQuantite)
                .sum();
    }

    /**
     * Obtenir le total du panier
     */
    public BigDecimal getCartTotal() {
        return cartItems.stream()
                .map(item -> item.getPrix().multiply(BigDecimal.valueOf(item.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Vider le panier
     */
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Vérifier si le panier est vide
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public Collection<Panier> getAllPaniers() {
        return panierRepository.findAll();
    }
    
    /**
     * Supprimer un panier par ID
     */
    public void deletePanier(Long id) {
        panierRepository.deleteById(id);
    }
}
