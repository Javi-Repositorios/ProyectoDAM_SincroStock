package com.proyecto.server_backend.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.server_backend.modelos.Articulo;
import com.proyecto.server_backend.modelos.Cliente;
import com.proyecto.server_backend.modelos.LineasPedido;
import com.proyecto.server_backend.modelos.Pedido;
import com.proyecto.server_backend.modelos.Trabajador;
import com.proyecto.server_backend.repositorios.ArticuloRepositorio;
import com.proyecto.server_backend.repositorios.ClienteRepositorio;
import com.proyecto.server_backend.repositorios.PedidoRepositorio;
import com.proyecto.server_backend.repositorios.TrabajadorRepositorio;

import jakarta.transaction.Transactional;

/**@author Javier Martinez Sodric
 * El servicio de Pedidos. Utiliza el servcio de validacion y necesita todos los otros repositorios, cliente, trabajador, articulo
 */
@Service
public class ServicioPedidos {

    @Autowired private PedidoRepositorio pedidoRepo;
    @Autowired private ClienteRepositorio repoCliente;
    @Autowired private TrabajadorRepositorio repoTrabajador;
    @Autowired private ArticuloRepositorio repoArticulo;
    @Autowired private ServicioValidacion servicioValidacion;

    
    public List<Pedido> listarPedidos()
    {
        return pedidoRepo.findAllByOrderByFechaDesc();
    }
    
    
    public List<Pedido> listarPorVendedor(String username) 
    {
        return pedidoRepo.findByVendedorUsernameOrderByFechaDesc(username);
    }

    @Transactional
    public Pedido guardarPedido(Pedido pedido) 
    {
        // Validación 
        if (!servicioValidacion.esPedidoValido(pedido)) 
        {
            throw new IllegalArgumentException("VALIDACION_FALLIDA");
        }

        // Recuperar entidades 
        Trabajador v = repoTrabajador.findByUsername(pedido.getVendedor().getUsername()).orElseThrow(() -> new RuntimeException("VENDEDOR_NO_ENCONTRADO"));
        
        Cliente c = repoCliente.findById(pedido.getCliente().getNif()).orElseThrow(() -> new RuntimeException("CLIENTE_NO_ENCONTRADO"));

        // Copia de lineas
        List<LineasPedido> lineasOriginales = new ArrayList<>(pedido.getLineas());
        
        //Asignacion
        pedido.setFecha(LocalDateTime.now());
        pedido.setVendedor(v);
        pedido.setCliente(c);
        pedido.setLineas(new ArrayList<>()); 

        // 3. Persistencia inicial (necesaria para que las líneas tengan un ID de pedido al cual referenciar)
        Pedido pPersistido = pedidoRepo.saveAndFlush(pedido);

        // 4. Procesar líneas y actualizar stock
        double total = 0;
        
        for (LineasPedido linea : lineasOriginales) 
        {
        	//Obtener el articulo de la linea
            Articulo art = repoArticulo.findById(linea.getArticulo().getId_articulo()).orElseThrow(() -> new RuntimeException("ARTICULO_NO_ENCONTRADO"));
            
            //DESCONTAR STOCK DEL ARTICULO
            art.setStock_disponible(art.getStock_disponible() - linea.getCantidad());
            repoArticulo.save(art);

            
            // Vincular línea con el pedido y el artículo real
            linea.setPedido(pPersistido);  // como estamos usando Spring Hibernate se vincula la referencia con el objeto 
            linea.setArticulo(art);				// ** quedan vinculados el numero de pedido y el numero de articulo a traves de los repositorios
            										
            // Calculo incremental del total de lineas del pedido 
            total += (linea.getPrecioVenta() * linea.getCantidad());
            
            //ADICION DE LA LINEA AL PEDIDO
            pPersistido.getLineas().add(linea);
        }

        //GUARDAR TOTAL Y FINALIZAR
        pPersistido.setTotal(total);
        return pedidoRepo.save(pPersistido);
    }

    public List<LineasPedido> obtenerLineasPorId(Long id) {
    
    	 // lista vacia
        List<LineasPedido> listaResultado = new ArrayList<>();
        
    	// buscar el pedido
        Optional<Pedido> pedidoOp = pedidoRepo.findById(id);        
        // comprobar
        if (!pedidoOp.isPresent())
        {
            throw new RuntimeException("PEDIDO_NO_ENCONTRADO");
        }       
        //asignar
        Pedido pedido = pedidoOp.get();
        
        //recorrer
        for (LineasPedido linea : pedido.getLineas()) 
        {
        	//añadir
            listaResultado.add(linea);
        }
        //devolver
        return listaResultado;
    
    }
    
    //TODOS PEDIDOS
    public List<Pedido> obtenerHistorialGlobal() 
    {
        return pedidoRepo.obtenerTodosLosPedidos();
    }

    
    
    public Articulo obtenerProductoMasVendido() 
    {
        List<Articulo> resultados = repoArticulo.buscarProductoMasVendido(PageRequest.of(0, 1));
        
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public Trabajador obtenerVendedorMasExitoso() 
    {
        List<Trabajador> resultados = repoTrabajador.buscarVendedorConMasVentas(PageRequest.of(0, 1));
        
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
