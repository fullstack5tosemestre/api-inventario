# API Inventario — SmartLogix

Microservicio REST responsable de la gestión del inventario de SmartLogix: productos, sucursales (branches) y bodegas (warehouses). Construido con Spring Boot 3.3.4, Spring Data JPA, MySQL y Liquibase. Puerto: **8081**.

Swagger UI: `http://localhost:8081/swagger-ui.html`

---

## Responsabilidad

Este microservicio gestiona exclusivamente el dominio de inventario:

- **Productos** (`/api/v1/products`): nombre, SKU, stock, bodega asociada.
- **Sucursales** (`/api/v1/branches`): puntos de venta o distribución.
- **Bodegas** (`/api/v1/warehouses`): almacenes físicos donde se guardan los productos.

No contiene lógica de pedidos ni de usuarios. La separación de responsabilidades permite escalar o modificar el inventario de forma independiente.

---

## Arquitectura

```
api-inventario
├── Controller (REST)   ← Recibe peticiones HTTP, delega al Service
│   ├── ProductController   /api/v1/products
│   ├── BranchController    /api/v1/branches
│   └── WarehouseController /api/v1/warehouses
├── Service (lógica)    ← Orquesta operaciones de negocio
│   ├── ProductService
│   ├── BranchService
│   └── WarehouseService
├── Repository (JPA)    ← Abstracción de persistencia con Spring Data
│   ├── ProductRepository
│   ├── BranchRepository
│   └── WarehouseRepository
└── Model (entidades)   ← Mapeo ORM con JPA/Hibernate
    ├── Product
    ├── Branch
    └── Warehouse
```

### Patrones de diseño aplicados

**1. MVC (Model-View-Controller)**
La arquitectura separa claramente las capas: los `Controller` manejan las solicitudes HTTP y devuelven respuestas, los `Service` contienen la lógica de negocio, y los `Model` representan las entidades del dominio. Esta separación facilita el mantenimiento y las pruebas unitarias de cada capa de forma independiente.

**2. Repository Pattern**
`ProductRepository`, `BranchRepository` y `WarehouseRepository` extienden `JpaRepository`, proveyendo una interfaz uniforme para acceder a la persistencia sin exponer los detalles de la base de datos. Permite cambiar la tecnología de persistencia sin afectar la lógica de negocio.

---

## Estructura de directorios

```
api-inventario/
├── src/
│   ├── main/
│   │   ├── java/com/smartlogix/inventario/
│   │   │   ├── InventarioApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── BranchController.java
│   │   │   │   └── WarehouseController.java
│   │   │   ├── model/
│   │   │   │   ├── Product.java
│   │   │   │   ├── Branch.java
│   │   │   │   └── Warehouse.java
│   │   │   ├── repository/
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── BranchRepository.java
│   │   │   │   └── WarehouseRepository.java
│   │   │   └── service/
│   │   │       ├── ProductService.java
│   │   │       ├── BranchService.java
│   │   │       └── WarehouseService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/changelog/            # Migraciones Liquibase
│   └── test/
│       ├── java/
│       └── resources/application-test.properties
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Modelos de datos

### Product

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador autoincremental |
| name | String | Nombre del producto |
| sku | String | Código SKU único |
| stock | int | Cantidad disponible en bodega |
| inWarehouse | Warehouse | Bodega donde está almacenado (FK) |

### Branch (Sucursal)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador autoincremental |
| name | String | Nombre de la sucursal |

### Warehouse (Bodega)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador autoincremental |
| name | String | Nombre de la bodega |

---

## Endpoints REST

### Productos — `/api/v1/products`

| Método | Ruta | Descripción | Respuesta |
|--------|------|-------------|-----------|
| GET | `/api/v1/products` | Listar todos los productos | 200 / 204 |
| GET | `/api/v1/products/{id}` | Obtener producto por ID | 200 / 404 |
| GET | `/api/v1/products/by-id/?ids=1,2` | Obtener productos por lista de IDs | 200 |
| POST | `/api/v1/products` | Crear producto | 201 |
| PUT | `/api/v1/products/{id}` | Actualizar producto | 200 / 404 |
| DELETE | `/api/v1/products/{id}` | Eliminar producto | 204 / 404 |

**Ejemplo POST `/api/v1/products`:**

```json
{
  "name": "Monitor LG 27\"",
  "sku": "MON-LG-27",
  "stock": 10,
  "inWarehouse": { "id": 1 }
}
```

**Respuesta 201:**

```json
{
  "id": 5,
  "name": "Monitor LG 27\"",
  "sku": "MON-LG-27",
  "stock": 10,
  "inWarehouse": { "id": 1, "name": "Bodega Central" }
}
```

---

### Sucursales — `/api/v1/branches`

| Método | Ruta | Descripción | Respuesta |
|--------|------|-------------|-----------|
| GET | `/api/v1/branches` | Listar sucursales | 200 / 204 |
| GET | `/api/v1/branches/{id}` | Obtener sucursal por ID | 200 / 404 |
| POST | `/api/v1/branches` | Crear sucursal | 201 |
| PUT | `/api/v1/branches/{id}` | Actualizar sucursal | 200 / 404 |
| DELETE | `/api/v1/branches/{id}` | Eliminar sucursal | 204 / 404 |

---

### Bodegas — `/api/v1/warehouses`

| Método | Ruta | Descripción | Respuesta |
|--------|------|-------------|-----------|
| GET | `/api/v1/warehouses` | Listar bodegas | 200 / 204 |
| GET | `/api/v1/warehouses/{id}` | Obtener bodega por ID | 200 / 404 |
| POST | `/api/v1/warehouses` | Crear bodega | 201 |
| PUT | `/api/v1/warehouses/{id}` | Actualizar bodega | 200 / 404 |
| DELETE | `/api/v1/warehouses/{id}` | Eliminar bodega | 204 / 404 |

---

## Dependencias principales (`pom.xml`)

| Artefacto | Versión | Propósito |
|-----------|---------|-----------|
| spring-boot-starter-parent | 3.3.4 | BOM de Spring Boot |
| spring-boot-starter-web | — | API REST con Spring MVC |
| spring-boot-starter-data-jpa | — | Persistencia con Hibernate |
| mysql-connector-j | — | Driver MySQL |
| liquibase-core | — | Migraciones de base de datos |
| lombok | — | Reducción de código boilerplate |
| springdoc-openapi-starter-webmvc-ui | 2.5.0 | Swagger UI |
| h2 (test) | — | Base de datos en memoria para pruebas |

---

## Instalación y ejecución

**Requisitos previos:** Java 17, Maven 3.8+, MySQL 8.

```bash
# 1. Configurar base de datos
# Crear un usuario MySQL o usar root. La base de datos se crea automáticamente
# gracias a createDatabaseIfNotExist=true en la URL.

# 2. Compilar
./mvnw clean package -DskipTests

# 3. Ejecutar
./mvnw spring-boot:run
```

Variables de entorno (sobreescriben application.properties):

| Variable | Por defecto | Descripción |
|----------|-------------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/db_inventario?...` | URL de conexión MySQL |
| `SPRING_DATASOURCE_USERNAME` | `root` | Usuario MySQL |
| `SPRING_DATASOURCE_PASSWORD` | *(vacío)* | Contraseña MySQL |

### Con Docker

```bash
docker build -t smartlogix-inventario .
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/db_inventario?createDatabaseIfNotExist=true \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  smartlogix-inventario
```

---

## Pruebas

Las pruebas unitarias utilizan H2 (base de datos en memoria) para no depender de MySQL:

```bash
# Ejecutar pruebas
./mvnw test

# Reporte HTML de cobertura (si se configura JaCoCo):
# target/site/jacoco/index.html

# Reporte de Surefire:
# target/surefire-reports/
```

La cobertura objetivo es ≥ 60% sobre las funcionalidades del servicio.

---

## Persistencia

Las migraciones de base de datos son gestionadas por **Liquibase**. El changelog principal está en `src/main/resources/db/changelog/db.changelog-master.yaml`. Al iniciar la aplicación, Liquibase aplica automáticamente las migraciones pendientes.

Para pruebas, el perfil `test` utiliza H2 (configurado en `src/test/resources/application-test.properties`), lo que garantiza que las pruebas sean reproducibles sin necesidad de una base de datos externa.

---

## Estrategia de branching

| Rama | Propósito |
|------|-----------|
| `main` | Código en producción |
| `develop` | Integración de cambios |
| `feature/*` | Nuevas funcionalidades |
| `fix/*` | Corrección de bugs |
