package com.vente_en_ligne.plateforme_e_commerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO pour transférer les données d'un élément de commande
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElementCommandeDTO {
    private Long id;
    
    @NotNull(message = "L'ID du produit est requis")
    private Long produitId;
    
    private String nomProduit;
    
    @NotNull(message = "La quantité est requise")
    @Positive(message = "La quantité doit être positive")
    private Integer quantite;
    
    private BigDecimal prixUnitaire;
    
    private BigDecimal sousTotal;
}
