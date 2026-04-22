package com.proyecto.server_backend.servicios;

import org.springframework.stereotype.Service;

import com.proyecto.server_backend.modelos.Articulo;

@Service
public class ServicioValidacion {
	
	public boolean esArticuloValido(Articulo articulo)
	{
		boolean esValido = false;
		
		if (articulo.getPrecio() > 0 || articulo.getStock_disponible() > 0) {
			
			esValido = true;
        }
		
		return esValido;
	}

}
