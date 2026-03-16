package com.vente_en_ligne.plateforme_e_commerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ElementCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    private Commande commande; // <- Relation avec Commande

    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

    // Constructeurs
    public ElementCommande() {}

    public ElementCommande(Produit produit, Commande commande, int quantite, BigDecimal prixUnitaire, BigDecimal sousTotal) {
        this.produit = produit;
        this.commande = commande;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = sousTotal;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; } // <- setter ajouté

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public BigDecimal getSousTotal() { return sousTotal; }
    public void setSousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; }
}