# Virtual Pet API Backend

API para administrar un sistema de mascotas virtuales interactivo, donde los usuarios pueden registrar, gestionar e interactuar con Gremlins. Desarrollado con **Spring Boot**.

---

## Tabla de Contenidos

1. [Estructura del Proyecto](#estructura-del-proyecto)
2. [Requisitos Previos](#requisitos-previos)
3. [Dependencias Principales](#dependencias-principales)
4. [Configuración](#configuración)
5. [Ejecución](#ejecución)
   - [Con Maven](#usando-maven)
   - [Con Docker](#usando-docker)
6. [Documentación de la API](#documentación-de-la-api)
7. [Endpoints Principales](#endpoints-principales)
8. [Autor](#autor)

---

## Estructura del Proyecto

```plaintext
src/
  ├── main/
  │   ├── java/
  │   │   ├── config/           // Configuración de seguridad, JWT y Swagger
  │   │   ├── controllers/      // Controladores REST
  │   │   ├── dto/              // Objetos de transferencia de datos
  │   │   ├── exception/        // Manejo de excepciones personalizadas
  │   │   ├── models/           // Entidades principales
  │   │   ├── repositories/     // Persistencia de datos con JPA
  │   │   └── services/         // Lógica de negocio
  │   ├── resources/
  │   │   └── application.properties
  └── test/                     // Pruebas unitarias
```

---

## Requisitos Previos

- **Java JDK 23**
- **Maven**
- **Docker** 
- **Base de datos SQL** 

---

## Dependencias Principales

| Dependencia        | Descripción                          |
|--------------------|--------------------------------------|
| Spring Boot        | Framework principal del proyecto    |
| JWT                | Autenticación basada en tokens      |
| Swagger            | Documentación de la API            |
| Spring Security    | Configuración de roles y seguridad |
| Spring Data JPA    | Interacción con la base de datos    |

---

## Instalación

Para clonar y ejecutar este proyecto en tu entorno local:

### Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/virtual-pet-api.git
cd virtual-pet-api


### Base de Datos

Edita el archivo `application.properties` con las credenciales de tu base de datos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/virtual_pet_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```

### JWT

Configura las claves secretas en `application.properties`:

```properties
jwt.secret=tu_clave_secreta
jwt.expiration=3600000
```

---

## Ejecución

### Usando Maven

1. Compila e instala el proyecto:
   ```bash
   mvn clean install
   ```

2. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

### Usando Docker

1. Construye y ejecuta el contenedor:
   ```bash
   docker-compose up --build
   ```

2. Accede a la API en [http://localhost:8080](http://localhost:8080).

---

## Documentación de la API

Accede a la documentación Swagger en:  
[http://localhost:8080/webjars/swagger-ui/index.html](http://localhost:8080/webjars/swagger-ui/index.html)

---

## Endpoints Principales

### Autenticación

| Método | Endpoint       | Descripción                  |
|--------|----------------|------------------------------|
| POST   | `/login`       | Inicia sesión.              |
| POST   | `/register`    | Registra un nuevo usuario.  |

### Mascotas

| Método | Endpoint          | Descripción                                |
|--------|-------------------|--------------------------------------------|
| GET    | `/pets`           | Obtiene todas las mascotas (admin) o las propias. |
| POST   | `/pets/create`    | Crea una nueva mascota.                   |
| PUT    | `/pets/{id}/update` | Actualiza el estado de una mascota.       |
| DELETE | `/pets/{id}/delete` | Elimina una mascota.                     |
---


