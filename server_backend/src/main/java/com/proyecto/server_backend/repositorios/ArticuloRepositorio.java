package com.proyecto.server_backend.repositorios;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.server_backend.modelos.Articulo;


/**
 * Interfaz DTO para acceder a db por el atributo marcado con @Id integer en el modelo y obtener Articulo
 */
@Repository
public interface ArticuloRepositorio extends JpaRepository<Articulo, Integer> {
	
	@Query("SELECT l.articulo FROM LineasPedido l GROUP BY l.articulo ORDER BY SUM(l.cantidad) DESC")
	List<Articulo> buscarProductoMasVendido(Pageable pageable);
}
