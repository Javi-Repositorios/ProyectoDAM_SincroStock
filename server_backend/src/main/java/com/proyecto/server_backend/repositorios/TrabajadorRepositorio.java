package com.proyecto.server_backend.repositorios;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.server_backend.modelos.Trabajador;



@Repository
public interface TrabajadorRepositorio extends JpaRepository<Trabajador, String> {
	
    Optional<Trabajador> findByUsername(String username);
        
    @Query("SELECT p.vendedor FROM Pedido p GROUP BY p.vendedor ORDER BY COUNT(p) DESC")
    List<Trabajador> buscarVendedorConMasVentas(Pageable pageable);
}

