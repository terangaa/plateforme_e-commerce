package com.vente_en_ligne.plateforme_e_commerce.config;

import com.vente_en_ligne.plateforme_e_commerce.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 🔐 Encoder mot de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Configuration principale
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // CSRF désactivé pour API
                        .ignoringRequestMatchers("/auth/**") // Ignore CSRF for login endpoint
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use sessions for HTML pages
                )
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/auth/login", "/auth/api/login").permitAll() // Login endpoints

                        // HTML protégé - use hasRole() which adds ROLE_ prefix automatically
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/proprietaire/**").hasRole("PROPRIETAIRE")
                        .requestMatchers("/client/**").hasRole("CLIENT")

                        // API REST
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/proprietaire/**").hasRole("PROPRIETAIRE")
                        .requestMatchers("/api/client/**").hasRole("CLIENT")

                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}