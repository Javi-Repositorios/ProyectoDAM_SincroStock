package com.proyecto.server_backend.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.server_backend.modelos.Cliente;
import com.proyecto.server_backend.repositorios.ClienteRepositorio;

@Service
public class ServicioClientes {


	@Autowired 
	private ClienteRepositorio clienteRepositorio;	

	@Autowired
	private ServicioValidacion servicioValidacion;

	public List<Cliente> listarTodos()
	{
		return clienteRepositorio.findAll();
	}

	public Cliente guardar(Cliente cliente)
	{
		if (!servicioValidacion.esClienteValido(cliente)) 
		{
			throw new IllegalArgumentException("DATOS_INVALIDOS");
		}

		return clienteRepositorio.save(cliente);
	}

	public void borrar(String nif)
	{
		clienteRepositorio.deleteById(nif);
	}

	public Optional<Cliente> actualizar(String nif, Cliente clienteEditado)
	{
		if (!servicioValidacion.esClienteValido(clienteEditado)) 
		{
			throw new IllegalArgumentException("DATOS_INVALIDOS");
		}


		return clienteRepositorio.findById(nif).map(cliente -> {

			cliente.setNif( clienteEditado.getNif());
			cliente.setEmail(clienteEditado.getEmail());
			cliente.setNombre(clienteEditado.getNombre());
			cliente.setTelefono(clienteEditado.getTelefono());


			return clienteRepositorio.save(cliente);
		});
	}

}
