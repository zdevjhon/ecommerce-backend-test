package com.ecommerce.test.security;

import com.ecommerce.test.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioService usuarioService;

    public SecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF para APIs
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/productos/**", "/api/usuarios/**", "/api/carrito/**", "/api/pedidos/**", "/api/detalle-pedidos/**", "/api/authenticate/**")
                        .permitAll() // Permite acceso a estas rutas sin autenticación
                        .anyRequest().authenticated() // Requiere autenticación para otras rutas
                )
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                            // No redirigir, simplemente permitir acceso después del login
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .permitAll() // Permite acceso al formulario de login sin autenticación previa
                )
                .httpBasic(AbstractHttpConfigurer::disable); // Desactiva autenticación básica
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService); // Configura el servicio de usuarios
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // Sin codificación de contraseñas

        return new ProviderManager(authProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Permite acceso desde el frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Permite el envío de cookies y cabeceras de autorización

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Utiliza codificación de contraseñas sin operación
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioService; // Utiliza UsuarioService para cargar detalles de usuario
    }
}
