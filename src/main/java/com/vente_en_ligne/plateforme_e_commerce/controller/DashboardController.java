package com.vente_en_ligne.plateforme_e_commerce.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class DashboardController {

//    @GetMapping("/admin/dashboard")
//    public String adminDashboard() {
//        return "admin/admin-dashboard";
//    }

    @GetMapping("/proprietaire/dashboard")
    public String proprietaireDashboard() {
        return "proprietaire/proprietaire-dashboard";
    }

    @GetMapping("/client/dashboard")
    public String clientDashboard() {
        return "client/client-dashboard";
    }

    // ← Ici, méthode pour redirection après login
    @GetMapping("/postLogin")
    public void postLogin(HttpServletResponse response) throws IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/admin/dashboard");
        } else if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROPRIETAIRE"))) {
            response.sendRedirect("/proprietaire/dashboard");
        } else if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))) {
            response.sendRedirect("/client/dashboard");
        } else {
            response.sendRedirect("/login");
        }
    }
}