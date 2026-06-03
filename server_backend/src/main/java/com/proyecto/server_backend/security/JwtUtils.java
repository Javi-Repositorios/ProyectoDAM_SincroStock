package com.proyecto.server_backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.proyecto.server_backend.modelos.Rol;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @author Javier Martinez Sodric
 * <h1>Utilidad para JSON Web Token (JWT)</h1>
 * <p>
 * Esta clase es un componente gestionado por Spring que proporciona métodos 
 * para generar, parsear y validar tokens JWT.
 * Al ser un componente, permite la inyección de la clave secreta y el tiempo de expiración desde el archivo 
 * de propiedades de la aplicación.
 * </p>
 */
@Component
public class JwtUtils {

    // Se cargan desde src/main/resources/application.properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT para un nombre de usuario y sus roles.
     * * @param username El nombre de usuario (subject) del token.
     * @param roles Conjunto de roles del usuario para incluir como claims.
     * @return Un String que representa el JWT compacto.
     */
    public String generarToken(String username, Set<Rol> roles) {
        String rolesString = roles.stream()
                                  .map(Rol::getRol)
                                  .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesString)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(obtenerKey())
                .compact();
    }

    /**
     * Crea la clave de firma a partir del secreto configurado.
     * * @return Una instancia de {@link SecretKey} para algoritmos HMAC.
     */
    private SecretKey obtenerKey() 
    {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el nombre de usuario (subject) del token.
     * * @param token El token JWT.
     * @return El nombre de usuario contenido en el token.
     */
    public String getUsernameFromToken(String token) 
    {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extrae los roles almacenados en los claims del token.
     * * @param token El token JWT.
     * @return String con los roles separados por comas, o null si el token es inválido.
     */
    public String validarYObtenerRol(String token) 
    {   	
    	String respuesta = null;
    	
        try 
        {
           respuesta = Jwts.parserBuilder()
                    .setSigningKey(obtenerKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles", String.class);
        }
        catch (Exception e) 
        {
            respuesta= null;
        }
        return respuesta;
    }

    /**
     * Valida la integridad y expiración del token.
     * * @param token El token a validar.
     * @return true si el token es válido y no ha expirado; false en caso contrario.
     */
    public boolean validarToken(String token) 
    {
    	boolean esValido = false;
        try 
        {
            Jwts.parserBuilder()
                .setSigningKey(obtenerKey())
                .build()
                .parseClaimsJws(token);
            esValido = true;
        } 
        catch (Exception e) 
        {
        	
        }
        return esValido;
    }
}
