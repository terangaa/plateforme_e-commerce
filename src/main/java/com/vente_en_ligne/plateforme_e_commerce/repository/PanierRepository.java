package com.vente_en_ligne.plateforme_e_commerce.repository;

import com.vente_en_ligne.plateforme_e_commerce.dto.PanierDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.Panier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PanierRepository extends JpaRepository<Panier,Long> {
}
