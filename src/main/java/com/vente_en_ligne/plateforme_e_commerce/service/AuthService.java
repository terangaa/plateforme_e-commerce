package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.dto.RegisterRequest;
import com.vente_en_ligne.plateforme_e_commerce.entity.Role;
import com.vente_en_ligne.plateforme_e_commerce.entity.User;
import com.vente_en_ligne.plateforme_e_commerce.repository.UserRepository;
import com.vente_en_ligne.plateforme_e_commerce.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Inscription d'un nouvel utilisateur
     */
    @Transactional
    public Map<String, Object> register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setTelephone(request.getTelephone());
        user.setAdresse(request.getAdresse());
        user.setVille(request.getVille());
        user.setCodePostal(request.getCodePostal());
        user.setPays(request.getPays());

        // 🔹 Assigner le rôle envoyé ou par défaut
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole(Role.CLIENT); // rôle par défaut
        }

        user.setActif(true);
        userRepository.save(user);

        String jwt = jwtService.generateToken(user.getEmail());

        return Map.of(
                "success", true,
                "token", jwt,
                "role", user.getRole().name()
        );
    }
    /**
     * Connexion d'un utilisateur
     */
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Email ou mot de passe incorrect");
            return response;
        }

        User user = userOpt.get();

        if (!user.getActif()) {
            response.put("success", false);
            response.put("message", "Compte désactivé");
            return response;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("success", false);
            response.put("message", "Email ou mot de passe incorrect");
            return response;
        }

        // Mise à jour de la date de modification
        user.setDateModification(LocalDateTime.now());
        userRepository.save(user);

        log.info("Utilisateur connecté: {}", user.getEmail());

        response.put("success", true);
        response.put("message", "Connexion réussie");
        response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "nom", user.getNom(),
                "prenom", user.getPrenom(),
                "role", user.getRole().name()
        ));

        return response;
    }

    /**
     * Obtenir un utilisateur par email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Obtenir un utilisateur par ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Vérifier si un email existe
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Mettre à jour le profil utilisateur
     */
    @Transactional
    public Map<String, Object> updateProfile(Long userId, Map<String, String> updates) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Utilisateur non trouvé");
            return response;
        }

        User user = userOpt.get();

        if (updates.containsKey("nom")) {
            user.setNom(updates.get("nom"));
        }
        if (updates.containsKey("prenom")) {
            user.setPrenom(updates.get("prenom"));
        }
        if (updates.containsKey("telephone")) {
            user.setTelephone(updates.get("telephone"));
        }
        if (updates.containsKey("adresse")) {
            user.setAdresse(updates.get("adresse"));
        }
        if (updates.containsKey("ville")) {
            user.setVille(updates.get("ville"));
        }
        if (updates.containsKey("codePostal")) {
            user.setCodePostal(updates.get("codePostal"));
        }
        if (updates.containsKey("pays")) {
            user.setPays(updates.get("pays"));
        }

        user.setDateModification(LocalDateTime.now());
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Profil mis à jour");
        response.put("user", user);

        return response;
    }

    /**
     * Changer le mot de passe
     */
    @Transactional
    public Map<String, Object> changePassword(Long userId, String oldPassword, String newPassword) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Utilisateur non trouvé");
            return response;
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            response.put("success", false);
            response.put("message", "Ancien mot de passe incorrect");
            return response;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setDateModification(LocalDateTime.now());
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Mot de passe changé avec succès");

        return response;
    }

    public User authenticate(String email, String password) {
        // 1️⃣ Récupérer l'utilisateur par email
        User user = userRepository.findByEmail(email)
                .orElse(null); // renvoie null si pas trouvé

        if (user == null) {
            return null; // utilisateur inexistant
        }

        // 2️⃣ Vérifier que l'utilisateur est actif
        if (!user.getActif()) {
            return null; // utilisateur désactivé
        }

        // 3️⃣ Vérifier le mot de passe avec BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (!matches) {
            return null; // mot de passe incorrect
        }

        // 4️⃣ Tout est correct → renvoyer l'utilisateur
        return user;
    }
}
