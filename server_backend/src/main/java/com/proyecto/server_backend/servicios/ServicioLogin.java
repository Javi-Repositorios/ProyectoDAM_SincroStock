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



/**
 * @author Javier Martinez Sodric
 * El servicio de login se encarga de autenticar los datos de registro, extrayendo el nombre del trabajador mandando por parametro, y bucandolo en la base de datos,
 * Después utiliza el encoder para cotejar las contraseñas y si son correctas, crea y devuelve un Map del token, username y roles
 */
@Service
public class ServicioLogin {
    @Autowired
    private TrabajadorRepositorio repositorio;

    @Autowired
    private BCryptPasswordEncoder encoder; // El encriptador
    
    @Autowired
    private JwtUtils jwtUtils;
    
 // --- LOGIN ---
    public Map<String, String> autenticar(Trabajador datos) 
    {
        Map<String, String> respuesta = null;
        
        //  Usar findById porque username es @Id
        Optional<Trabajador> trabajadorEncontrado = repositorio.findById(datos.getUsername());

        if (trabajadorEncontrado.isPresent()) {
            Trabajador trabajador = trabajadorEncontrado.get();

            if (encoder.matches(datos.getPassword(), trabajador.getPassword())) 
            {
                // Convertir el Set de roles a un String para la respuesta del JSON
                String rolesString = trabajador.getRoles().stream()
                                               .map(Rol::getRol)
                                               .collect(Collectors.joining(","));
                
                respuesta = new HashMap<>(); // Inicializamos el mapa solo si las credenciales son válidas
                
                // Pasar el Set de roles al generador de token
                respuesta.put("token", jwtUtils.generarToken(trabajador.getUsername(), trabajador.getRoles()));
                respuesta.put("roles", rolesString);
                respuesta.put("username", trabajador.getUsername());
            }
        }
        
        return respuesta; 
    }
    
    
    public String encriptar(String clave)
    {
    	return encoder.encode(clave);
    }

    // --- REGISTRO ---
    public Trabajador guardarEmpleado(Trabajador nuevoTrabajador)
    {
        // Encriptar la clave simple del form
        String claveEncriptada = encoder.encode(nuevoTrabajador.getPassword());
        
        // Sustituir la simple por la encriptada
        nuevoTrabajador.setPassword(claveEncriptada);
        
        // Save
        return repositorio.save(nuevoTrabajador);
    }
}