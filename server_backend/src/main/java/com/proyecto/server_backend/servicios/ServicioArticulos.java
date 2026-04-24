package com.proyecto.server_backend.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.server_backend.modelos.Articulo;

import com.proyecto.server_backend.repositorios.ArticuloRepositorio;

/**
 * Logica de negocio del modelo Articulo. Puente entre controlador y repositorio.
 */
@Service
public class ServicioArticulos {

	@Autowired 
	private ArticuloRepositorio articuloRepositorio;	
	
	@Autowired
	private ServicioValidacion servicioValidacion;
	
	
	//LEER 
    public Optional<Articulo> buscarPorId(int id) {
        return articuloRepositorio.findById(id);
    }

    //LEER TODOS
    public List<Articulo> listarTodos()
    {
    	return articuloRepositorio.findAll(); 
    }
    	   
    //GUARDAR
    public Articulo guardar(Articulo articulo) 
    {
        // Si no da valido
        if (!servicioValidacion.esArticuloValido(articulo)) 
        {
            throw new IllegalArgumentException("DATOS_INVALIDOS");
        }

        return articuloRepositorio.save(articulo);
    }
    
    // BORRAR
    public void borrar(int id) 
    { 
    	articuloRepositorio.deleteById(id); 
    }

    //ACTUALIZAR
    public Optional<Articulo> actualizar(int id, Articulo nuevosDatos) {
        // 1. Buscamos el artículo existente por ID
        return articuloRepositorio.findById(id).map(articulo -> {
            
            // 2. Actualizamos todos los campos necesarios
            articulo.setNombre(nuevosDatos.getNombre());
            articulo.setPrecio(nuevosDatos.getPrecio());
            articulo.setStock_disponible(nuevosDatos.getStock_disponible());

            // Si en el futuro añades más campos, deben ir aquí
            
            // 3. Guardamos los cambios
            return articuloRepositorio.save(articulo);
        });
    }
}
