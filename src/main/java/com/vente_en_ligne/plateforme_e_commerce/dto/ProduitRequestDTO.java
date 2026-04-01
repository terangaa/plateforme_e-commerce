package com.vente_en_ligne.plateforme_e_commerce.dto;

import com.vente_en_ligne.plateforme_e_commerce.entity.Categorie;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProduitRequestDTO {
    private String nom;
    private String description;
    private BigDecimal prix;
    private Integer stock;
    private BigDecimal quantite;
    private String imageUrl;
    private Boolean actif;
    private Long categorieId;     // juste l'ID
    private Long proprietaireId;  // juste l'ID

}