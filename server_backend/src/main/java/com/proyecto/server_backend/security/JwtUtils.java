package com.proyecto.server_backend.security;


import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

@Component
/**
 * <h1>Utilidad para JSON Web Token (JWT)</h1>
 * <p>
 * Esta clase proporciona métodos para generar, parsear y validar tokens JWT
 * utilizados en el proceso de autenticación.
 * Utiliza el algoritmo <b>HS256</b> para la firma de los tokens.
 * </p>
 */
public class JwtUtils {
	
	
    private String jwtSecret = "tu_clave_secreta_super_larga_de_al_menos_32_caracteres_123456";
    private int jwtExpirationMs = 86400000; // 24 horas

    /**
     * Genera un token JWT para un nombre de usuario específico.
     * * @param username El nombre de usuario (subject) del token.
     * @return Un String que representa el JWT compacto.
     */
 // 1. GENERAR: Ahora recibe también el rol (o lista de roles)
    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol) // IMPORTANTE: Metemos el rol aquí
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(obtenerKey())
                .compact();
    }

    /**
     * Genera la clave de firma a partir del secreto definido.
     * @return Una instancia de {@link SecretKey}.
     */
    private SecretKey obtenerKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
  
    
    /**
     * Extrae el nombre de usuario contenido en el "subject" del token.
     * * @param token El token JWT.
     * @return El nombre de usuario extraído.
     */
 // 2. EXTRAER USERNAME: Para que Spring sepa quién es
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Saca el "sub" (username)
    }
    
 // 3. EXTRAER ROL: Para que Spring sepa qué puede hacer
    public String getRolFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(obtenerKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("rol", String.class); // Saca el claim "rol"
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Valida si un token es estructuralmente correcto y no ha expirado.
     * * @param token El token a validar.
     * @return true si es válido, false en caso contrario.
     */
 // 4. VALIDAR: Solo comprueba si el token es legítimo
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(obtenerKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
