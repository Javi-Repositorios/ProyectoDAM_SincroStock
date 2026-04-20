package com.proyecto.server_backend.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.proyecto.server_backend.modelos.Rol;
import com.proyecto.server_backend.modelos.Trabajador;
import com.proyecto.server_backend.servicios.ServicioLogin;
import com.proyecto.server_backend.servicios.ServicioRoles;
import com.proyecto.server_backend.servicios.ServicioTrabajadores;



@RestController
@RequestMapping("/api/trabajadores")
// Quitamos el CrossOrigin de aquí porque ya está en SecurityConfig
public class TrabajadorControlador {

    @Autowired private ServicioTrabajadores trabajadorServicio;
    @Autowired private ServicioRoles rolServicio;
    @Autowired private ServicioLogin loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Trabajador datos) {
        Map<String, String> resultado = loginService.autenticar(datos);
        return resultado != null ? ResponseEntity.ok(resultado) : ResponseEntity.status(401).body("Error");
    }

    // Hemos quitado todos los @PreAuthorize. La seguridad ahora la lleva SecurityConfig.
    
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        return ResponseEntity.ok(trabajadorServicio.listarTodos());
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> actualizar(@PathVariable("username") String username, @RequestBody Trabajador datos) {
        return trabajadorServicio.actualizar(username, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> borrar(@PathVariable("username") String username) {
        trabajadorServicio.borrar(username);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Trabajador t) {
        return ResponseEntity.ok(trabajadorServicio.guardar(t));
    }

    @GetMapping("/roles-disponibles")
    public ResponseEntity<List<Rol>> obtenerRoles() {
        return ResponseEntity.ok(rolServicio.listarTodos());
    }
}