package com.vente_en_ligne.plateforme_e_commerce.repository;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des produits
 */
@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCategorieId(Long categorieId);
    List<Produit> findByCategorieIdAndActiveTrue(Long categorieId);
    // Récupérer uniquement les produits actifs
    List<Produit> findByActiveTrue();
    // Les 10 derniers produits par date de création
    List<Produit> findTop10ByOrderByCreatedAtDesc();

    List<Produit> findByProprietaire(User proprietaire);

    @Query("SELECT p FROM Produit p JOIN FETCH p.categorie")
    List<Produit> findAllWithCategorie();
    // On prend seulement le dernier
   // Produit findTopByOrderByCreatedAtDesc();
}
