package com.proyecto.server_backend.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.server_backend.modelos.Articulo;
import com.proyecto.server_backend.servicios.ServicioArticulos;
import com.proyecto.server_backend.servicios.ServicioPedidos;


@RestController
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*")
public class ArticuloControlador {
	
	@Autowired private ServicioArticulos servicioArticulos;

	@Autowired private ServicioPedidos servicioPedidos;
	
	//LISTAR
	@GetMapping
	public ResponseEntity<?> obtenerArticulos()
	{
		return ResponseEntity.ok(servicioArticulos.listarTodos());
	}
	
	@PostMapping
	public ResponseEntity<?> crearArticulo(@RequestBody Articulo articulo) {
	    ResponseEntity<?> respuesta;

	    try {
	        // El servicio hace el trabajo y valida (Nodos 2, 5 y 7)
	        Articulo guardado = servicioArticulos.guardar(articulo);
	        respuesta = ResponseEntity.status(201).body(guardado);
	    } 
	    catch (IllegalArgumentException e) {
	        // Capturamos el error de lógica del servicio (Nodo 4)
	        respuesta = ResponseEntity.status(422).body("Error: Precio o stock negativos.");
	    } 
	    catch (DataIntegrityViolationException e) {
	        // Error de BD, por ejemplo si ya existe (Nodo 6)
	        respuesta = ResponseEntity.status(409).body("El artículo ya existe.");
	    } 
	    catch (Exception e) {
	        // Cualquier otro drama (500)
	        respuesta = ResponseEntity.status(500).body("Error interno.");
	    }

	    return respuesta; // ÚNICO RETURN. Prometido.
	}
	
	//BORRAR POR ID 
	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrar(@PathVariable("id") int id_articulo)
	{
		servicioArticulos.borrar(id_articulo);
		return ResponseEntity.ok().build();
	}
	
	
	//ACTUALIZAR: APARTE DE PASAR EL ID POR VARIABLE EN LA RUTA FRONTEND;
	//				TIENE QUE PASAR EL OBJETO A GUARDAR EN EL BODY
	@PutMapping("/{id}") 
	public ResponseEntity<?> actualizarArticulo(@PathVariable("id") int id_articulo, @RequestBody Articulo articulo) {
	    // Es importante asegurar que el ID de la URL se asigne al objeto
	    articulo.setId_articulo(id_articulo); 
	    
	    return servicioArticulos.actualizar(id_articulo, articulo)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/mas-vendido")
    public ResponseEntity<Articulo> getProductoTop() {
        return ResponseEntity.ok(servicioPedidos.obtenerProductoMasVendido());
    }

}
