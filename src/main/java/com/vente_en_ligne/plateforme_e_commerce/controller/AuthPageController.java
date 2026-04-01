package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.entity.User;
import com.vente_en_ligne.plateforme_e_commerce.security.JwtService;
import com.vente_en_ligne.plateforme_e_commerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    /**
     * Form-based login for HTML pages - sets up session authentication
     */
    @PostMapping("/login")
    public String loginForm(@RequestParam String email,
                            @RequestParam String password,
                            HttpServletRequest request) {
        
        User user = authService.authenticate(email, password);
        
        if (user == null) {
            return "redirect:/login?error";
        }

        // Load user details
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getActif(), true, true, true,
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        // Create authentication token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // Set authentication in security context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        // Create session and store security context
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        // Generate JWT token and store in session for API calls
        String jwtToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
        session.setAttribute("jwt_token", jwtToken);
        session.setAttribute("user_role", user.getRole().name());
        session.setAttribute("user_email", user.getEmail());

        // Redirect based on role
        return switch (user.getRole().name()) {
            case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
            case "ROLE_PROPRIETAIRE" -> "redirect:/proprietaire/dashboard";
            case "ROLE_CLIENT" -> "redirect:/client/dashboard";
            default -> "redirect:/";
        };
    }

    /**
     * API login endpoint - returns JWT token (kept for API clients)
     */
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginApi(@RequestBody Map<String, String> credentials,
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
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Email ou mot de passe invalide"
            ));
        }

        // Set up session authentication for HTML page navigation
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

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        session.setAttribute("jwt_token", token);
        session.setAttribute("user_role", user.getRole().name());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", token,
                "role", user.getRole().name(),
                "id", user.getId(),
                "email", user.getEmail(),
                "nom", user.getNom(),
                "prenom", user.getPrenom(),
                "message", "Connexion réussie"
        ));
    }
}
