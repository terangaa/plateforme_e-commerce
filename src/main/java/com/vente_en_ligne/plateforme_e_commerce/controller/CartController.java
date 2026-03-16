package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.dto.CartItemDTO;
import com.vente_en_ligne.plateforme_e_commerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion du panier avec validation
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Obtenir tous les éléments du panier
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart() {
        try {
            List<CartItemDTO> items = cartService.getCartItems();
            Map<String, Object> response = new HashMap<>();
            response.put("items", items);
            response.put("total", cartService.getCartTotal());
            response.put("itemCount", cartService.getItemCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Ajouter un produit au panier avec validation
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam(required = false) Long produitId,
            @RequestParam(required = false, defaultValue = "1") Integer quantite) {

        // Validation des paramètres
        if (produitId == null) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "L'ID du produit est requis");
        }

        if (quantite == null || quantite <= 0) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "La quantité doit être supérieure à 0");
        }

        if (quantite > 100) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "La quantité maximale autorisée est de 100");
        }

        try {
            cartService.addToCart(produitId, quantite);

            // Renvoie tout le panier à jour
            Map<String, Object> response = new HashMap<>();
            response.put("items", cartService.getCartItems()); // ← liste complète
            response.put("total", cartService.getCartTotal());
            response.put("itemCount", cartService.getItemCount());
            response.put("success", true);
            response.put("message", "Produit ajouté au panier");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    /**
     * Modifier la quantité d'un produit dans le panier avec validation
     */
    @PutMapping("/update/{produitId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long produitId,
            @RequestParam(required = false) Integer quantite) {
        
        // Validation des paramètres
        if (produitId == null) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "L'ID du produit est requis");
        }
        
        if (quantite == null) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "La quantité est requise");
        }
        
        if (quantite < 0) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "La quantité ne peut pas être négative");
        }
        
        if (quantite > 100) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "La quantité maximale autorisée est de 100");
        }

        try {
            CartItemDTO item = cartService.updateQuantity(produitId, quantite);
            Map<String, Object> response = new HashMap<>();
            response.put("item", item);
            response.put("success", true);
            response.put("cartTotal", cartService.getCartTotal());
            response.put("itemCount", cartService.getItemCount());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Supprimer un produit du panier avec validation
     */
    @DeleteMapping("/remove/{produitId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long produitId) {
        
        // Validation du paramètre
        if (produitId == null) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "L'ID du produit est requis");
        }

        try {
            cartService.removeFromCart(produitId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Produit retiré du panier");
            response.put("total", cartService.getCartTotal());
            response.put("itemCount", cartService.getItemCount());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Vider le panier
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        try {
            cartService.clearCart();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Panier vidé");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Obtenir le nombre d'éléments dans le panier
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCartCount() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("count", cartService.getItemCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Obtenir le total du panier
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> getCartTotal() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("total", cartService.getCartTotal());
            response.put("itemCount", cartService.getItemCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Vérifier si le panier est valide
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCart() {
        try {
            List<CartItemDTO> items = cartService.getCartItems();
            Map<String, Object> response = new HashMap<>();
            response.put("isValid", !items.isEmpty());
            response.put("itemCount", cartService.getItemCount());
            response.put("total", cartService.getCartTotal());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Méthode utilitaire pour créer des réponses d'erreur
     */
    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        response.put("status", status.value());
        return ResponseEntity.status(status).body(response);
    }
}
