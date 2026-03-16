package com.vente_en_ligne.plateforme_e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
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
    private BigDecimal total;

    /**
     * Calcul du total de la commande
     */
    public BigDecimal getTotal() {
        if (elements == null || elements.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (ElementCommande el : elements) {
            total = total.add(el.getSousTotal()); // sousTotal est BigDecimal
        }
        return total.setScale(2, BigDecimal.ROUND_HALF_UP); // 2 décimales
    }

    /**
     * Ajouter un élément à la commande
     */
    public void addElement(ElementCommande el) {
        el.setCommande(this); // assure la relation bidirectionnelle
        elements.add(el);
    }

    /**
     * Retirer un élément de la commande
     */
    public void removeElement(ElementCommande el) {
        el.setCommande(null); // casse la relation bidirectionnelle
        elements.remove(el);
    }

    public void setTotal(BigDecimal total) {
        this.total = total;  // ⚠️ il faut assigner ici
    }
}