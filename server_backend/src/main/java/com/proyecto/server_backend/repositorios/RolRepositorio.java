package com.proyecto.server_backend.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.server_backend.modelos.Rol;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, String> {
	
	Optional<Rol> findByRol(String rol);
}