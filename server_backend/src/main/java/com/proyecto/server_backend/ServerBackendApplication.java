package com.proyecto.server_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Clase principal que inicia la aplicación Spring Boot.
 * <p>
 * Esta clase se encarga de configurar el contexto de la aplicación,
 * activar la autoconfiguración de Spring y escanear los componentes
 * definidos en el paquete raíz.
 * </p>
 * * @author TuNombre
 * @version 1.0
 */
public class ServerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerBackendApplication.class, args);
	}

}
