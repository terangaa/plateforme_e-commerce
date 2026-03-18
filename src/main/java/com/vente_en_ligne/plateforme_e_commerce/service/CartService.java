package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.dto.CartItemDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion du panier avec stockage en session
 */
@Service
@SessionScope
@RequiredArgsConstructor
public class CartService {

    private final ProduitRepository produitRepository;
    private final List<CartItemDTO> cartItems = new ArrayList<>();

    /**
     * Ajouter un produit au panier
     */
    @Transactional
    public CartItemDTO addToCart(Long produitId, Integer quantite) {
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
        Optional<CartItemDTO> existingItem = cartItems.stream()
                .filter(item -> item.getProduitId().equals(produitId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Mettre à jour la quantité
            CartItemDTO item = existingItem.get();
            int nouvelleQuantite = item.getQuantite() + quantite;

            if (produit.getStock() != null && produit.getStock() < nouvelleQuantite) {
                throw new IllegalArgumentException("Stock insuffisant pour cette quantité");
            }

            item.setQuantite(nouvelleQuantite);
            return item;
        } else {
            // Ajouter un nouveau produit
            CartItemDTO newItem = new CartItemDTO(
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
    public CartItemDTO updateQuantity(Long produitId, Integer quantite) {
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

        Optional<CartItemDTO> itemOpt = cartItems.stream()
                .filter(item -> item.getProduitId().equals(produitId))
                .findFirst();

        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Produit non trouvé dans le panier");
        }

        CartItemDTO item = itemOpt.get();
        item.setQuantite(quantite);
        return item;
    }

    /**
     * Supprimer un produit du panier
     */
    @Transactional
    public CartItemDTO removeFromCart(Long produitId) {
        Optional<CartItemDTO> itemOpt = cartItems.stream()
                .filter(item -> item.getProduitId().equals(produitId))
                .findFirst();

        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Produit non trouvé dans le panier");
        }

        CartItemDTO removedItem = itemOpt.get();
        cartItems.remove(removedItem);
        return removedItem;
    }

    /**
     * Obtenir tous les éléments du panier
     */
    public List<CartItemDTO> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    /**
     * Obtenir le nombre d'éléments dans le panier
     */
    public int getItemCount() {
        return cartItems.stream()
                .mapToInt(CartItemDTO::getQuantite)
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
}
