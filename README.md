# Sistema de Gestión Empresarial (SGE)

## Descripción del Proyecto
Este proyecto es una aplicación **SGE (Sistema de Gestión Empresarial)** desarrollada mediante una arquitectura de microservicios contenerizados.
 El sistema permite la gestión integral de entidades clave: **Trabajadores, Roles, Clientes, Pedidos, Líneas de Pedido y Artículos**.

La aplicación sigue un diseño robusto basado en el patrón **Vista-Controlador-Servicio-Repositorio**, garantizando una estructura limpia, mantenible y escalable.

## Arquitectura del Sistema




La solución está compuesta por tres contenedores gestionados mediante **Docker**:

1.  **Frontend Proxy (Nginx):** Gestiona el enrutamiento del tráfico hacia el backend.
2.  **Backend (Spring Boot):** Núcleo lógico que implementa la API RESTful.
3.  **Base de Datos (PostgreSQL):** Almacenamiento persistente de datos relacionales.
4.  **ORM Hibernate:** Modelado y acceso a datos.

*Puedes ver el detalle de la configuración en el archivo docker-compose, nginx.conf y SecurityConfig

## Características Principales
* **Seguridad:** Autenticación basada en **Tokens (JWT)** con filtros de seguridad personalizados.
* **Control de Acceso:** Sistema de **acceso granular** basado en roles para la protección de recursos.
* **Operaciones CRUD:** Implementación completa (Crear, Leer, Actualizar, Borrar) para todas las entidades.
* **Filtros de Búsqueda:** Motor de búsqueda avanzado integrado en el backend.

*Puedes ver los casos de uso critico en la Documentación de las pruebas

## Metodología de Desarrollo
Se ha seguido una metodología **híbrida** que combina el **Modelo Incremental-Decreciente** con prácticas **Ágiles**:

* **Planificación:** Estructurada en **3 Sprints**.
* **Ejecución:** Cada sprint comprende 2 tareas principales.
* **Calidad:** Fase de pruebas dedicada al finalizar cada sprint para asegurar la integridad del sistema.

*Consulta el tablero Kanban

## Herramientas Utilizadas
* **Backend:** Java, Spring Boot.
* **Base de Datos:** PostgreSQL.
* **Proxy:** Nginx.
* **Contenerización:** Docker, Docker Desktop.
* **Control de Versiones:** Git, GitHub.

## Instalación y Despliegue
Para desplegar la aplicación localmente, asegúrate de tener **Docker Desktop** instalado y sigue estos pasos:

1.  Clona el repositorio:
    ```bash
    git clone <url-del-repositorio>
    ```
2.  Navega a la carpeta raíz del proyecto.
3.  Ejecuta el entorno con el siguiente comando:
    ```bash
    docker-compose up --build
    ```

---
4.  Tendrás que introducir el usuario principal en Postgres,  tienes el código necesario en la documentación.
    Lo puedes ver  en el Esquema Entidad Relación y siguientes.

    Una vez levantado el stack, se entra por localhost.

5.  Actualmente las rutas están apuntando al servidor real (midominio.com) . Deberás modificarlas para que funcione en localhost.
