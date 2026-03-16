package com.vente_en_ligne.plateforme_e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Column(name = "prix_unitaire", nullable = false)
    private double prixUnitaire;

    @Column(name = "quantite", nullable = false)
    private int quantite;

    @Column(name = "sous_total", nullable = false)
    private double sousTotal;

    @PrePersist
    @PreUpdate
    public void calculerSousTotal() {
        this.sousTotal = this.prixUnitaire * this.quantite;
    }
}