package com.proyecto.server_backend.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proyecto.server_backend.modelos.Pedido;


/**
 * Interfaz DTO de la entidad Pedido. 
 * Contiene metodos personalizados para devolver los pedidos de un cliente, de un vendededor, o de todos
 */
public interface PedidoRepositorio  extends JpaRepository<Pedido,Long> {
	
		Pedido findByClienteNombre(String username);
	
	    List<Pedido> findByClienteNombreOrderByFechaDesc(String username);
	    
	    List<Pedido> findByVendedorUsernameOrderByFechaDesc(String username);
	    
		List<Pedido> findAllByOrderByFechaDesc();
		
		@Query("SELECT p FROM Pedido p ORDER BY p.fecha DESC")
		List<Pedido> obtenerTodosLosPedidos();
	    
	   
	 
	}

