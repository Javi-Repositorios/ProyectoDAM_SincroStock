package com.proyecto.server_backend.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.server_backend.modelos.Rol;
import com.proyecto.server_backend.repositorios.RolRepositorio;

@Service
public class ServicioRoles {

    @Autowired 
    private RolRepositorio repositorio;

    public List<Rol> listarTodos() {
        return repositorio.findAll();
    }
    
}