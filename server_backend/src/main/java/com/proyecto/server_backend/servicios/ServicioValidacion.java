package com.proyecto.server_backend.servicios;

import org.springframework.stereotype.Service;

import com.proyecto.server_backend.modelos.Articulo;
import com.proyecto.server_backend.modelos.Cliente;

@Service
public class ServicioValidacion {
	
	public boolean esArticuloValido(Articulo articulo)
	{
		boolean esValido = false;
		
		if (articulo.getPrecio() > 0 && articulo.getStock_disponible() > 0) {
			
			esValido = true;
        }
		
		return esValido;
	}
	
	
	public boolean esClienteValido(Cliente c) {
        if (c == null) return false;
        
        // Expresión regular para 8 números y 1 letra mayúscula
        boolean nifValido = c.getNif() != null && c.getNif().matches("^[0-9]{8}[A-Z]$");
        boolean nombreValido = c.getNombre() != null && !c.getNombre().trim().isEmpty() && c.getNombre().length() <= 100;
        boolean emailValido = c.getEmail() != null && c.getEmail().contains("@") && c.getEmail().length() <= 100;
        boolean telValido = c.getTelefono() != null && !c.getTelefono().trim().isEmpty() && c.getTelefono().length() <= 15;

        return nifValido && nombreValido && emailValido && telValido;
    }

}
