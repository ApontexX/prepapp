# PrepApp — Backend

API REST desarrollada con Java y Spring Boot para PrepApp, una plataforma que permite a los usuarios organizar apuntes en carpetas y bloques, integrando inteligencia artificial para mejorar la productividad y el estudio.

## Tecnologías

- Java 17
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL (Railway)
- Maven

## Funcionalidades

- Registro e inicio de sesión con JWT
- Gestión de carpetas por usuario
- Gestión de bloques de apuntes dentro de carpetas
- Base de datos en la nube con Railway
- Seguridad por capas — cada usuario solo accede a sus propios datos

## Estructura del proyecto
src/main/java/com/miguel/prepapp/
├── controller/     # Endpoints de la API
├── model/          # Entidades JPA
├── repository/     # Acceso a base de datos
├── service/        # Lógica de negocio
└── security/       # JWT, filtros y configuración de seguridad

## Configuración

1. Clona el repositorio
2. Copia `src/main/resources/application.properties.example` y renómbralo a `application.properties`
3. Rellena las credenciales de tu base de datos PostgreSQL
4. Ejecuta el proyecto con:

```bash
./mvnw spring-boot:run
```

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /api/auth/register | Registrar usuario |
| POST | /api/auth/login | Iniciar sesión |
| GET | /api/folders | Listar carpetas |
| POST | /api/folders | Crear carpeta |
| PUT | /api/folders/{id} | Editar carpeta |
| DELETE | /api/folders/{id} | Eliminar carpeta |
| GET | /api/blocks/folder/{id} | Listar bloques |
| POST | /api/blocks/folder/{id} | Crear bloque |
| PUT | /api/blocks/{id} | Editar bloque |
| DELETE | /api/blocks/{id} | Eliminar bloque |

## Frontend

El frontend de esta aplicación está disponible en [prepapp-frontend](https://github.com/ApontexX/prepapp-frontend)