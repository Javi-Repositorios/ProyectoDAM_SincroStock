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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;
    
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
        // 1. Validación 
        if (!servicioValidacion.esPedidoValido(pedido)) {
            throw new IllegalArgumentException("VALIDACION_FALLIDA");
        }

        // 2. Recuperar entidades 
        Trabajador v = repoTrabajador.findByUsername(pedido.getVendedor().getUsername()).orElseThrow(() -> new RuntimeException("VENDEDOR_NO_ENCONTRADO"));
        Cliente c = repoCliente.findById(pedido.getCliente().getNif()).orElseThrow(() -> new RuntimeException("CLIENTE_NO_ENCONTRADO"));

        List<LineasPedido> lineasOriginales = new ArrayList<>(pedido.getLineas());
        
        pedido.setFecha(LocalDateTime.now());
        pedido.setVendedor(v);
        pedido.setCliente(c);
        pedido.setLineas(new ArrayList<>()); 

        // 3. Persistencia inicial
        Pedido pPersistido = pedidoRepo.saveAndFlush(pedido);

        // ========================================================================
        // borrar caché de Hibernate para este hilo
        // ========================================================================
        entityManager.flush(); // Asegura que el pedido se escribe en Postgres
        entityManager.clear(); // Desacopla TODO de la memoria (obliga a ir a la BD)
        // ========================================================================

        // Volvemos a recuperar el pedido persistido porque el clear() lo desalojó de la memoria
        pPersistido = pedidoRepo.findById(pPersistido.getId()).get();

        double total = 0;
        
        for (LineasPedido linea : lineasOriginales) 
        {
            // 
            Articulo art = repoArticulo.findByIdForUpdate(linea.getArticulo().getId_articulo())
                .orElseThrow(() -> new RuntimeException("ARTICULO_NO_ENCONTRADO"));
            
            System.out.println(">>> [HILO " + Thread.currentThread().getName() + "] Entra al LOCK. Stock leído: " + art.getStock_disponible());

            // Validar stock real de la base de datos
            if (art.getStock_disponible() < linea.getCantidad()) 
            {
                System.err.println(">>> [HILO " + Thread.currentThread().getName() + "] ¡SIN STOCK! Solicitado: " + linea.getCantidad() + ", Disponible: " + art.getStock_disponible());
                throw new RuntimeException("Stock insuficiente para el artículo ID: " + art.getId_articulo());
            }

            // Restar stock
            art.setStock_disponible(art.getStock_disponible() - linea.getCantidad());
            repoArticulo.saveAndFlush(art); 

            // Vincular
            linea.setPedido(pPersistido);  
            linea.setArticulo(art);				
            										
            total += (linea.getPrecioVenta() * linea.getCantidad());
            pPersistido.getLineas().add(linea);

            // Delay con el candado echado
            try 
            {
                System.out.println(">>> [HILO " + Thread.currentThread().getName() + "] Durmiendo 10 seg con el cerrojo puesto...");     
                Thread.sleep(10000); 
                System.out.println(">>> [HILO " + Thread.currentThread().getName() + "] Despierta.");
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        
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
