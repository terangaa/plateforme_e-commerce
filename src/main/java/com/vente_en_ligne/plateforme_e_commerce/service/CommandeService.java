package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.dto.ElementCommandeDTO;
import com.vente_en_ligne.plateforme_e_commerce.dto.RequeteCommandeDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.*;
import com.vente_en_ligne.plateforme_e_commerce.repository.CommandeRepository;
import com.vente_en_ligne.plateforme_e_commerce.repository.ProduitRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des commandes
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;
    private final EmailService emailService;

    public List<Commande> obtenirToutesLesCommandes() {
        return commandeRepository.findAll();
    }

    public Commande obtenirCommandeParId(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
    }

    public List<Commande> obtenirCommandesParStatut(StatutCommande statut) {
        return commandeRepository.findByStatut(statut);
    }

    @Transactional
    public Commande creerCommande(RequeteCommandeDTO req) throws MessagingException {
        Commande commande = new Commande();
        commande.setNomClient(req.getClientNom());
        commande.setEmailClient(req.getClientEmail());
        commande.setTelephoneClient(req.getClientTelephone());
        commande.setAdresseClient(req.getAdresse());
        commande.setStatut(String.valueOf(StatutCommande.EN_ATTENTE));

        Commande finalCommande = commande;
        List<ElementCommande> elements = req.getProduits().stream().map(dto -> {
            Produit produit = produitRepository.findById(dto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + dto.getProduitId()));

            ElementCommande el = new ElementCommande();
            el.setProduit(produit);
            el.setQuantite(dto.getQuantite());
            el.setPrixUnitaire(produit.getPrix());

            BigDecimal sousTotal = produit.getPrix().multiply(BigDecimal.valueOf(dto.getQuantite()));
            el.setSousTotal(sousTotal);

            el.setCommande(finalCommande); // ⚠️ Lien commande → élément

            return el;
        }).collect(Collectors.toList());

        commande.setElements(elements);

        // Calcul total
        BigDecimal total = elements.stream()
                .map(ElementCommande::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        commande.setTotal(total);

        // Sauvegarde complète de la commande avec éléments
        commande = commandeRepository.save(commande);

        // Email au propriétaire uniquement
        emailService.envoyerNotificationCommandeAuProprietaire(commande);

        log.info("Commande {} créée avec succès", commande.getId());
        return commande;
    }

    @Transactional
    public Commande mettreAJourStatutCommande(Long id, StatutCommande statut) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
        commande.setStatut(String.valueOf(statut));
        return commandeRepository.save(commande);
    }

    public List<Commande> obtenirCommandesParEmailClient(String email) {
        return commandeRepository.findByEmailClient(email);
    }

    // Méthode pour récupérer une commande par son id
    public Commande getCommandeById(Long orderId) {
        return commandeRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'id : " + orderId));
    }

    public Collection<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'id : " + id));

        // Dissocier ou supprimer les éléments de commande pour éviter les problèmes de clé étrangère
        if (commande.getElements() != null) {
            commande.getElements().forEach(el -> el.setCommande(null));
            commande.getElements().clear();
        }

        // Supprimer la commande
        commandeRepository.delete(commande);
    }

    public void save(Commande commande) {
        commandeRepository.save(commande);
    }

    public void updateStatut(Long id, String statut) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        commande.setStatut(statut); // ou Enum.valueOf si enum
        commandeRepository.save(commande);
    }
}
