package com.proyecto.server_backend.servicios;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.server_backend.modelos.Rol;
import com.proyecto.server_backend.modelos.Trabajador;
import com.proyecto.server_backend.repositorios.TrabajadorRepositorio;
import com.proyecto.server_backend.security.JwtUtils;




@Service
public class ServicioLogin {
    @Autowired
    private TrabajadorRepositorio repositorio;

    @Autowired
    private BCryptPasswordEncoder encoder; // Inyectamos el encriptador
    
    @Autowired
    private JwtUtils jwtUtils;
    
    // --- LOGIN ---
    public Map<String, String> autenticar(Trabajador datos) {
        // 1. Usar findById porque username es @Id
        Optional<Trabajador> trabajadorEncontrado = repositorio.findById(datos.getUsername());

        if (trabajadorEncontrado.isPresent()) {
            Trabajador trabajador = trabajadorEncontrado.get();

            if (encoder.matches(datos.getPassword(), trabajador.getPassword())) {
                // 2. Convertir el Set de roles a un String para la respuesta del JSON
                String rolesString = trabajador.getRoles().stream()
                                               .map(Rol::getRol)
                                               .collect(Collectors.joining(","));
                
                Map<String, String> respuesta = new HashMap<>();
                // 3. Pasar el Set de roles al generador de token
                respuesta.put("token", jwtUtils.generarToken(trabajador.getUsername(), trabajador.getRoles()));
                respuesta.put("roles", rolesString);
                respuesta.put("username", trabajador.getUsername());
                return respuesta;
            }
        }
        return null; 
    }
    
    
    public String encriptar(String clave)
    {
    	return encoder.encode(clave);
    }

    // --- REGISTRO ---
    public Trabajador guardarEmpleado(Trabajador nuevoTrabajador)
    {
        // 1. Encriptamos la clave antes de guardar
        String claveEncriptada = encoder.encode(nuevoTrabajador.getPassword());
        
        // 2. Se la volvemos a poner al objeto
        nuevoTrabajador.setPassword(claveEncriptada);
        
        // 3. Guardamos en la BD (ahora se guarda ilegible)
        return repositorio.save(nuevoTrabajador);
    }
}