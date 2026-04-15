package com.proyecto.server_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
/**
 * <h1>Filtro de Autenticación JWT</h1>
 * <p>
 * Este filtro intercepta cada petición HTTP entrante para validar la presencia de un token 
 * JWT en la cabecera <code>Authorization</code>.
 * </p>
 * <p>
 * Si el token es válido, establece la autenticación en el contexto de seguridad de Spring, 
 * permitiendo que el usuario acceda a los recursos protegidos.
 * </p>
 * * @see OncePerRequestFilter
 * @see SecurityContextHolder
 */
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    /**
     * Utilidad para la generación, validación y extracción de datos del token JWT.
     */
    private JwtUtils jwtUtils;

    @Override
    /**
     * Realiza el filtrado interno de la petición para gestionar la seguridad por token.
     * <p>
     * El flujo de este método es:
     * Extrae la cabecera <code>Authorization</code>.
     * Verifica si el prefijo es <code>Bearer </code>.
     * Valida el token mediante {@link JwtUtils}.
     * Si es correcto, crea un objeto {@link UsernamePasswordAuthenticationToken} y 
     * lo guarda en el {@link SecurityContextHolder}.
     * </p>
     * * @param request El objeto de la petición HTTP.
     * @param response El objeto de la respuesta HTTP.
     * @param filterChain La cadena de filtros para continuar la ejecución.
     * @throws ServletException Si ocurre un error interno en el servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtils.validarToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}