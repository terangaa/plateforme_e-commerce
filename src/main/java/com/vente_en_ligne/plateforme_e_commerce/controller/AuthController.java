package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.dto.RegisterRequest;
import com.vente_en_ligne.plateforme_e_commerce.entity.User;
import com.vente_en_ligne.plateforme_e_commerce.security.JwtService;
import com.vente_en_ligne.plateforme_e_commerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    /** Inscription */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "L'email est requis"
            ));
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Le mot de passe doit contenir au moins 6 caractères"
            ));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Les mots de passe ne correspondent pas"
            ));
        }

        Map<String, Object> result = authService.register(request);
        return ResponseEntity.ok(result);
    }

    /** Connexion avec génération de token JWT + session setup */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials,
                                                     HttpServletRequest request) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email et mot de passe requis"
            ));
        }

        User user = authService.authenticate(email, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", "Email ou mot de passe invalide"
                    ));
        }

        // Setup session authentication for HTML page navigation
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getActif(), true, true, true,
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        // Generate JWT token
        String role = user.getRole().name();
        String token = jwtService.generateToken(user.getEmail(), role);
        session.setAttribute("jwt_token", token);
        session.setAttribute("user_role", role);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", token,
                "role", role,
                "id", user.getId(),
                "email", user.getEmail(),
                "nom", user.getNom(),
                "prenom", user.getPrenom(),
                "message", "Connexion réussie"
        ));
    }

    /** Vérifier si email existe */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(Map.of(
                "exists", exists
        ));
    }

    /** Déconnexion (côté client) */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Déconnexion réussie"
        ));
    }
}