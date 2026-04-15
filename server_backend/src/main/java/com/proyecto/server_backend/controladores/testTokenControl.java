
package com.proyecto.server_backend.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.server_backend.security.JwtUtils;



@RestController
public class testTokenControl {
	
	@Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/api/test/token")
    public String sacarToken() {
        // Generamos un token manual para probar. 
        return jwtUtils.generarToken("admin", "ROLE_ADMIN");
    }
}

