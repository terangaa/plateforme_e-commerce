package com.vente_en_ligne.plateforme_e_commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commande")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_client", nullable = false)
    private String nomClient;

    @Column(name = "email_client", nullable = false)
    private String emailClient;

    @Column(name = "telephone_client", nullable = false)
    private String telephoneClient;

    @Column(name = "adresse_client", nullable = false)
    private String adresseClient;

    @Column(name = "date_commande", nullable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();

    @Column(name = "statut", nullable = false)
    private String statut = "EN_ATTENTE";

    // Relation avec les éléments de la commande
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementCommande> elements = new ArrayList<>();

    @Column(name = "total", scale = 2, precision = 10)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statutPaiement = StatutPaiement.NON_PAYEE;

    /**
     * Calcul le total de la commande à partir des éléments de commande.
     */
    public void calculerTotal() {
        if (elements == null || elements.isEmpty()) {
            this.total = BigDecimal.ZERO;
        } else {
            BigDecimal sommeTotale = BigDecimal.ZERO;
            for (ElementCommande el : this.elements) {
                sommeTotale = sommeTotale.add(el.getSousTotal());
            }
            this.total = sommeTotale.setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * Ajoute un élément à la commande.
     * @param el L'élément de commande à ajouter.
     */
    public void addElement(ElementCommande el) {
        el.setCommande(this); // Assure la relation bidirectionnelle
        elements.add(el);
        calculerTotal(); // Recalcule le total
    }

    /**
     * Supprime un élément de la commande.
     * @param el L'élément de commande à retirer.
     */
    public void removeElement(ElementCommande el) {
        el.setCommande(null); // Casse la relation bidirectionnelle
        elements.remove(el);
        calculerTotal(); // Recalcule le total
    }
}