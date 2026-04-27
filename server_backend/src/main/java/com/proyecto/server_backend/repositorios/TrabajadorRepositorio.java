package com.proyecto.server_backend.repositorios;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.server_backend.modelos.Trabajador;



@Repository
public interface TrabajadorRepositorio extends JpaRepository<Trabajador, String> {

	
	 // Spring entiende esta nomenclatura automática: busca en Vendedor -> campo Username
    Optional<Trabajador> findByUsername(String username);
}

