package com.vente_en_ligne.plateforme_e_commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElementCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", scale = 2, precision = 10, nullable = false)
    private BigDecimal prixUnitaire;

    @Column(name = "sous_total", scale = 2, precision = 10, nullable = false)
    private BigDecimal sousTotal;

    /**
     * Calcule automatiquement le sous-total de cet élément.
     */
    public void calculerSousTotal() {
        if (prixUnitaire != null && quantite != null) {
            this.sousTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            this.sousTotal = BigDecimal.ZERO;
        }
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
        calculerSousTotal(); // Met à jour le sous-total à chaque changement de quantité
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        calculerSousTotal(); // Met à jour le sous-total à chaque changement de prix
    }
}