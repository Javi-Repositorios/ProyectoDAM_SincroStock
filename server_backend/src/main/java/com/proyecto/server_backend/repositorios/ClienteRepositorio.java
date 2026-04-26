package com.proyecto.server_backend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.server_backend.modelos.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

}
