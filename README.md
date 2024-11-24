Virtual Pet API Backend
Este proyecto es una API para manejar un sistema de mascotas virtuales donde los usuarios pueden interactuar con Gremlins. Los usuarios pueden registrarse, iniciar sesión y realizar diversas acciones con sus mascotas. El proyecto está desarrollado en Java utilizando Spring Boot.

Estructura del proyecto:
src/
  - main/
    - java/
      - S5/
        - T2/
          - virtual_pet_api_back/
            - VirtualPetApiBackApplication.java    // Punto de entrada de la aplicación.
            - config/                              // Configuraciones de seguridad, JWT y Swagger.
              - JwtFilter.java
              - SecurityConfig.java
              - SwaggerConfig.java
              - WebConfig.java
            - controllers/                         // Controladores para usuarios y mascotas.
              - PetController.java
              - UserController.java
            - dto/                                 // Objetos de transferencia de datos.
              - LoginRequest.java
              - PetActionRequest.java
              - PetRequest.java
              - RegisterRequest.java
            - exception/                           // Manejo de excepciones personalizadas.
              - AccessDeniedException.java
              - GlobalExceptionHandler.java
              - PetNotFoundException.java
              - UserNotFoundException.java
            - models/                              // Clases que representan las entidades del dominio.
              - Pet.java
              - PetType.java
              - User.java
              - UserPrincipal.java
              - UserType.java
            - repositories/                        // Interfaces de persistencia con base de datos.
              - PetRepository.java
              - UserRepository.java
            - services/                            // Lógica de negocio y servicios.
              - JWTService.java
              - MyUserDetailsService.java
              - PetService.java
              - PetStatusService.java
              - UserService.java
    - resources/
      - application.properties                    // Configuraciones principales de Spring Boot.
  - test/
    - java/
      - S5/
        - T2/
          - virtual_pet_api_back/
            - VirtualPetApiBackApplicationTests.java // Pruebas unitarias.
Requisitos previos:
Java JDK 23.
Maven para la gestión de dependencias.
Docker (opcional, para ejecutar en contenedor).
Una base de datos SQL configurada (como MySQL).

Dependencias principales
Spring Boot: Framework principal del proyecto.
JWT: Para la autenticación basada en tokens.
Swagger: Documentación de la API.
Spring Security: Para la configuración de roles y acceso.
Spring Data JPA: Para interacción con la base de datos.

Configuración
Modifica el archivo application.properties en src/main/resources para incluir la configuración de tu base de datos, por ejemplo:
spring.datasource.url=jdbc:mysql://localhost:3306/virtual_pet_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
Ejecuta el proyecto para asegurarte de que las tablas se generen correctamente en la base de datos.

Puesta en marcha:
Usando Maven
Navega al directorio raíz del proyecto.
Ejecuta:
mvn clean install
mvn spring-boot:run

Usando Docker
Asegúrate de que tienes un archivo Dockerfile y docker-compose.yml.
Construye la imagen:
docker-compose up --build
Accede a la API en http://localhost:8080.

Endpoints principales
Autenticación
POST /login: Inicia sesión con un usuario registrado.
POST /register: Crea un nuevo usuario.

Mascotas
GET /pets: Obtiene todas las mascotas (solo admin) o las del usuario autenticado.
POST /pets/create: Crea una nueva mascota.
PUT /pets/{id}/update: Actualiza el estado de una mascota.

Documentación de la API
La documentación Swagger está disponible en: http://localhost:8080/webjars/swagger-ui/index.html
