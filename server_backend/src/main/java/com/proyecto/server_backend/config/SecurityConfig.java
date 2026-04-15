package com.proyecto.server_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.proyecto.server_backend.security.JwtFilter;




@Configuration
@EnableWebSecurity
/**
 * <h1>Clase de configuración de seguridad.</h1>
 * <p>
 * Esta clase se encarga de:
 * 1. Definir los beans de seguridad (PasswordEncoder, SecurityFilterChain).
 * 2. Configurar el acceso a los endpoints de la API.
 * 3. Deshabilitar CSRF para pruebas locales si es necesario.
 *</p>
 */
public class SecurityConfig {

	/**
     * Filtro personalizado para la interceptación y validación de tokens JWT.
     */
    @Autowired
    private JwtFilter jwtFilter;

    
    /**
     * Configura la cadena de filtros de seguridad (Security Filter Chain).
     * <p>
     * Este método define:
     * La desactivación de CSRF(necesaria para APIs REST).
     * La política de sesión como STATELESS.
     * Las reglas de autorización de rutas (endpoints públicos vs privados).
     * La inserción del filtro JWT en el ciclo de vida de la petición.
     
     
     * </p>
     * * @param http Objeto {@link HttpSecurity} para configurar la seguridad web.
     * @return La cadena de filtros configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     * @see JwtFilter
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/api/auth/**", "/api/test/token").permitAll()
                .anyRequest().authenticated() // El resto pide Token
            );

        // Ponemos nuestro filtro antes del filtro de usuario/password de Spring
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
