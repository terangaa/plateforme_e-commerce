package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.dto.RegisterRequest;
import com.vente_en_ligne.plateforme_e_commerce.entity.Categorie;
import com.vente_en_ligne.plateforme_e_commerce.entity.Commande;
import com.vente_en_ligne.plateforme_e_commerce.entity.Panier;
import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import com.vente_en_ligne.plateforme_e_commerce.entity.User;
import com.vente_en_ligne.plateforme_e_commerce.service.AdminService;
import com.vente_en_ligne.plateforme_e_commerce.service.CategorieService;
import com.vente_en_ligne.plateforme_e_commerce.service.CommandeService;
import com.vente_en_ligne.plateforme_e_commerce.service.PanierService;
import com.vente_en_ligne.plateforme_e_commerce.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ProduitService produitService;
    private final CommandeService commandeService;
    private final PanierService panierService;
    private final CategorieService categorieService;
    private final PasswordEncoder passwordEncoder;

    // ─── DASHBOARD ───
    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        String username = (authentication != null) ? authentication.getName() : "Invité";
        model.addAttribute("username", username);

        List<Produit> produits = (List<Produit>) produitService.getAllProduits();
        List<Commande> commandes = (List<Commande>) commandeService.getAllCommandes();

        model.addAttribute("produits", produits);
        model.addAttribute("commandes", commandes);

        return "admin/admin-dashboard";
    }

    // ─── MODERN ADMIN PAGES ───
    
    // Page des produits moderne
    @GetMapping("/produits")
    public String adminProduits(Model model) {
        List<Produit> produits = (List<Produit>) produitService.getAllProduits();
        model.addAttribute("produits", produits);
        return "admin/admin-products";
    }
    
    // Page des utilisateurs moderne
    @GetMapping("/utilisateurs")
    public String adminUtilisateurs(Model model) {
        List<User> utilisateurs = adminService.getAllUsers();
        model.addAttribute("utilisateurs", utilisateurs);
        return "admin/admin-users";
    }
    
    // Page des commandes moderne
    @GetMapping("/commandes/liste")
    public String adminCommandes(Model model) {
        List<Commande> commandes = (List<Commande>) commandeService.getAllCommandes();
        model.addAttribute("commandes", commandes);
        return "commande";
    }
    
    // Page des paniers moderne
    @GetMapping("/panier")
    public String adminPanier(Model model) {
        List<Panier> paniers = (List<Panier>) panierService.getAllPaniers();
        model.addAttribute("paniers", paniers);
        return "admin/admin-carts";
    }
    
    // REST API - PANIERS
    @GetMapping("/api/paniers")
    @ResponseBody
    public ResponseEntity<List<Panier>> getAllPaniersApi() {
        List<Panier> paniers = (List<Panier>) panierService.getAllPaniers();
        return ResponseEntity.ok(paniers);
    }

    @DeleteMapping("/api/paniers/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePanierApi(@PathVariable Long id) {
        try {
            panierService.deletePanier(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Panier supprimé avec succès");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // REST API - PRODUITS (pour le JavaScript du dashboard admin)
    // ═══════════════════════════════════════════════════════════════
    
    @GetMapping("/api/produits")
    @ResponseBody
    public ResponseEntity<List<Produit>> getAllProduitsApi() {
        List<Produit> produits = (List<Produit>) produitService.getAllProduits();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/produits/{id}")
    @ResponseBody
    public ResponseEntity<Produit> getProduitByIdApi(@PathVariable Long id) {
        Produit produit = produitService.getProduitById(id);
        if (produit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produit);
    }

    @PostMapping("/produits")
    @ResponseBody
    public ResponseEntity<?> createProduitApi(@RequestBody Produit produit) {
        try {
            produitService.save(produit);
            return ResponseEntity.ok(produit);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/produits/{id}")
    @ResponseBody
    public ResponseEntity<?> updateProduitApi(@PathVariable Long id, @RequestBody Produit produit) {
        try {
            Produit existing = produitService.getProduitById(id);
            if (existing == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Produit non trouvé");
                return ResponseEntity.notFound().build();
            }
            produit.setId(id);
            produitService.update(produit);
            return ResponseEntity.ok(produit);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/produits/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteProduitApi(@PathVariable Long id) {
        try {
            produitService.softDeleteProduit(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Produit supprimé avec succès");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // REST API - UTILISATEURS (pour le JavaScript du dashboard admin)
    // ═══════════════════════════════════════════════════════════════
    
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsersApi() {
        List<User> users = adminService.getAllUsers();
        // Ne pas retourner le mot de passe
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserByIdApi(@PathVariable Long id) {
        User user = adminService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users")
    @ResponseBody
    public ResponseEntity<?> createUserApi(@RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setNom(request.getNom());
            user.setPrenom(request.getPrenom());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setTelephone(request.getTelephone());
            user.setActif(true);
            
            User savedUser = adminService.addUser(user);
            savedUser.setPassword(null);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<?> updateUserApi(@PathVariable Long id, @RequestBody User userData) {
        try {
            User existing = adminService.getUserById(id);
            if (existing == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Utilisateur non trouvé");
                return ResponseEntity.notFound().build();
            }
            
            existing.setNom(userData.getNom());
            existing.setPrenom(userData.getPrenom());
            existing.setEmail(userData.getEmail());
            existing.setTelephone(userData.getTelephone());
            existing.setActif(userData.getActif());
            existing.setRole(userData.getRole());
            
            // Only update password if provided
            if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(userData.getPassword()));
            }
            
            adminService.updateUser(existing);
            existing.setPassword(null);
            return ResponseEntity.ok(existing);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUserApi(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Utilisateur supprimé avec succès");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ─── PRODUITS (MODERNE) ───
    // ─── PRODUITS (MODERNE) ───
    @GetMapping("/produits/ajouter")
    public String formAjouterProduit(Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("categories", categorieService.getAllCategories());
        return "admin/product-add";
    }

    @PostMapping("/produits/ajouter")
    public String ajouterProduit(@ModelAttribute Produit produit,
                                 @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            String imageUrl = produitService.saveImage(imageFile); // méthode à créer dans service
            produit.setImageUrl(imageUrl);
        }
        produitService.save(produit);
        return "redirect:/admin/produits";
    }

    // ─── MODIFIER PRODUIT ───
    @GetMapping("/produits/modifier/{id}")
    public String formModifierProduit(@PathVariable Long id, Model model) {

        Produit produit = produitService.getProduitById(id); // 🔥 plus simple

        model.addAttribute("produit", produit);
        model.addAttribute("categories", categorieService.getAllCategories());

        return "admin/product-edit";
    }
    @PostMapping("/produits/modifier/{id}")
    public String modifierProduit(@PathVariable Long id,
                                  @ModelAttribute Produit produit,
                                  @RequestParam("imageFile") MultipartFile imageFile) {

        Produit existing = produitService.getProduitById(id);

        existing.setNom(produit.getNom());
        existing.setDescription(produit.getDescription());
        existing.setPrix(produit.getPrix());
        existing.setStock(produit.getStock());
        existing.setQuantite(produit.getQuantite());

        // 🔥 correction catégorie
        if (produit.getCategorie() != null && produit.getCategorie().getId() != null) {
            Categorie categorie = categorieService.getCategorieById(produit.getCategorie().getId());
            existing.setCategorie(categorie);
        }

        // image
        if (!imageFile.isEmpty()) {
            String imageUrl = produitService.saveImage(imageFile);
            existing.setImageUrl(imageUrl);
        }

        produitService.update(existing);

        return "redirect:/admin/produits";
    }
    // ─── SUPPRIMER PRODUIT ───
    @GetMapping("/admin/produits/delete/{id}")
    public String softDeleteProduit(@PathVariable Long id) {
        produitService.softDeleteProduit(id);
        return "redirect:/admin/produits";
    }

    // ─── UTILISATEURS ───
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/list-users";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = adminService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/form-user";
    }

    @GetMapping("/users/ajouter")
    public String formAjouterUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/form-user";
    }

    @PostMapping("/users/ajouter")
    public String addUser(@ModelAttribute User user) {
        adminService.addUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/modifier/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id);
        adminService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/supprimer/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // ─── COMMANDES ───
    @GetMapping("/commandes")
    public String listCommandes(Model model) {
        List<Commande> commandes = (List<Commande>) commandeService.getAllCommandes();
        model.addAttribute("commandes", commandes);
        return "commande/list-commandes";
    }

    // Formulaire pour ajouter une commande
    @GetMapping("/ajouter")
    public String addCommandeForm(Model model) {
        model.addAttribute("commande", new Commande());
        return "commande/form-commande";
    }

    // Sauvegarder une nouvelle commande ou modifier
    @PostMapping("/save")
    public String saveCommande(@ModelAttribute Commande commande) {
        commandeService.save(commande);
        return "redirect:/admin/commandes";
    }

    // Formulaire pour modifier une commande
    @GetMapping("/modifier/{id}")
    public String editCommandeForm(@PathVariable Long id, Model model) {
        Commande commande = commandeService.getCommandeById(id);
        model.addAttribute("commande", commande);
        return "commande/form-commande";
    }


    @GetMapping("/commandes/{id}")
    public String getCommande(@PathVariable Long id, Model model) {
        Commande commande = commandeService.getCommandeById(id);
        model.addAttribute("commande", commande);
        return "commande/detail-commande";
    }

    @PutMapping("/api/commandes/{id}/statut")
    @ResponseBody
    public ResponseEntity<?> updateCommandeStatut(@PathVariable Long id,
                                                  @RequestBody Map<String, String> body) {
        try {
            String statut = body.get("statut");
            commandeService.updateStatut(id, statut);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/commandes/supprimer/{id}")
    public String deleteCommande(@PathVariable Long id) {
        commandeService.delete(id);
        return "redirect:/commande/commandes";
    }
}