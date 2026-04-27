package com.proyecto.server_backend.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.server_backend.modelos.Pedido;



public interface PedidoRepositorio  extends JpaRepository<Pedido,Long> {
	
		Pedido findByClienteNombre(String username);
	 // Si tu clase Usuario tiene un campo 'username', usa este:
	    List<Pedido> findByClienteNombreOrderByFechaDesc(String username);
	    
	    List<Pedido> findByVendedorUsernameOrderByFechaDesc(String username);
	 
	}

