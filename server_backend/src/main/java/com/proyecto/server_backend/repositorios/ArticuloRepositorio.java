package com.proyecto.server_backend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.server_backend.modelos.Articulo;


/**
 * Interfaz DTO para acceder a db por el atributo marcado con @Id en el modelo y obtener Articulo
 */
@Repository
public interface ArticuloRepositorio extends JpaRepository<Articulo, Integer> {

}
