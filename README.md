# Perlitas de Eucarion

"Perlitas de Eucarion" es una pyme de bisutería que solucionó sus problemas de gestión manual con una app propia. El sistema centraliza inventario, ventas, encargos y reportes. Así, simplifica tareas, reduce errores y brinda a la dueña una visión organizada para controlar y proyectar mejor su negocio.

---

## Integrantes

- Yuliana Calvucura
- Luis Grossolli
- Catalina Salinas

---

## Aplicaciones requeridas

- Java JDK 21
- Maven
- Spring Boot 3.5.14
- MySQL (XAMPP)
- VS Code
- Postman

---

## Microservicios

| Microservicio | Puerto | Base de datos |
|---|---|---|
| service-auth | 8091 | db_seguridad_perlita |
| materiales | 8081 | db_materials |
| productos | 8082 | db_products |
| ventas | 8083 | db_ventas |
| pedidos | 8085 | db_orders |
| categories | 8084 | db_categories |
| proveedores | 8086 | db_suppliers |
| reportes | 8087 | db_reportes |
| galeria | 8088 | db_galeria |
| gastos | 8089 | db_gastos |
| service_calendario | 8090 | db_calendario |
| gateway | 9090 | - |

---

## Cómo ejecutar el proyecto

### 1. Requisitos previos
- Tener XAMPP corriendo con MySQL activo
- Tener Java 21 instalado

### 2. Clonar el repositorio
```bash
git clone https://github.com/lugrossolli-ux/perlitas_de_eucarion.git
```

### 3. Ejecutar cada microservicio (orden recomendado)
```bash
cd Perlita/service-auth && ./mvnw spring-boot:run
cd Perlita/materiales && ./mvnw spring-boot:run
cd Perlita/productos && ./mvnw spring-boot:run
cd Perlita/ventas && ./mvnw spring-boot:run
cd Perlita/pedidos && ./mvnw spring-boot:run
cd Perlita/categories && ./mvnw spring-boot:run
cd Perlita/proveedores && ./mvnw spring-boot:run
cd Perlita/reportes && ./mvnw spring-boot:run
cd Perlita/galeria && ./mvnw spring-boot:run
cd Perlita/gastos && ./mvnw spring-boot:run
cd Perlita/service_calendario && ./mvnw spring-boot:run
cd Perlita/gateway && ./mvnw spring-boot:run
```

Las bases de datos se crean automáticamente al iniciar cada microservicio.

### 4. Autenticación

Antes de consumir los endpoints, registre un usuario y obtenga un token JWT:

```bash
# Registrar usuario
POST http://localhost:9090/auth/registrar
{
  "nombreUsuario": "admin",
  "contrasena": "123456",
  "correo": "admin@perlitas.cl",
  "roles": ["ADMIN"]
}

# Iniciar sesión
POST http://localhost:9090/auth/login
{
  "nombreUsuario": "admin",
  "contrasena": "123456"
}
```

Use el token recibido en el header `Authorization: Bearer <token>` para todas las peticiones.

---

## Swagger UI

Cada microservicio expone su documentación OpenAPI a través del Gateway:

| Microservicio | Swagger UI |
|---|---|
| service-auth | http://localhost:9090/swagger-ui/auth.html |
| materiales | http://localhost:9090/swagger-ui/materiales.html |
| productos | http://localhost:9090/swagger-ui/productos.html |
| ventas | http://localhost:9090/swagger-ui/ventas.html |
| pedidos | http://localhost:9090/swagger-ui/pedidos.html |
| categories | http://localhost:9090/swagger-ui/categories.html |
| proveedores | http://localhost:9090/swagger-ui/proveedores.html |
| reportes | http://localhost:9090/swagger-ui/reportes.html |
| galeria | http://localhost:9090/swagger-ui/galeria.html |
| gastos | http://localhost:9090/swagger-ui/gastos.html |
| service_calendario | http://localhost:9090/swagger-ui/calendario.html |

---

## Endpoints disponibles

Todos los endpoints pueden consumirse directamente por su puerto o a través del Gateway en `http://localhost:9090`.

### Autenticación `http://localhost:9090/auth`

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /auth/registrar | Registra un nuevo usuario |
| POST | /auth/login | Inicia sesión y devuelve un token JWT |

### Materiales `http://localhost:9090/materiales`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /materiales | Lista todos los materiales |
| GET | /materiales/{id} | Busca material por ID |
| GET | /materiales/stock-bajo | Lista materiales con stock bajo |
| POST | /materiales | Crea un material |
| PUT | /materiales/{id} | Actualiza un material |
| DELETE | /materiales/{id} | Elimina un material |

### Productos `http://localhost:9090/productos`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /productos | Lista todos los productos |
| GET | /productos/activos | Lista productos activos |
| GET | /productos/{id} | Busca producto por ID |
| GET | /productos/{id}/materiales | Ver materiales del producto |
| POST | /productos | Crea un producto |
| POST | /productos/{id}/materiales | Agrega material al producto |
| PUT | /productos/{id} | Actualiza un producto |
| DELETE | /productos/{id} | Elimina un producto |

### Ventas `http://localhost:9090/ventas`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /ventas | Lista todas las ventas |
| GET | /ventas/{id} | Busca venta por ID |
| GET | /ventas/cliente/{clienteId} | Ventas por cliente |
| GET | /ventas/resumen/metodo-pago | Resumen por método de pago |
| POST | /ventas | Crea una venta |

### Pedidos `http://localhost:9090/pedidos`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /pedidos | Lista todos los pedidos |
| GET | /pedidos/{id} | Busca pedido por ID |
| GET | /pedidos/estado/{estado} | Pedidos por estado |
| GET | /pedidos/cliente/{clienteId} | Pedidos por cliente |
| POST | /pedidos | Crea un pedido |
| PUT | /pedidos/{id} | Actualiza un pedido |
| DELETE | /pedidos/{id} | Elimina un pedido |
| PATCH | /pedidos/{id}/estado?nuevoEstado=... | Cambia estado del pedido |

### Categorías `http://localhost:9090/categorias`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /categorias | Lista todas las categorías |
| GET | /categorias/activas | Lista categorías activas |
| GET | /categorias/{id} | Busca categoría por ID |
| GET | /categorias/obtenerProducto/{id} | Productos de una categoría |
| POST | /categorias | Crea una categoría |
| PUT | /categorias/{id} | Actualiza una categoría |
| DELETE | /categorias/{id} | Elimina una categoría |
| PATCH | /categorias/reasignar | Reasigna productos entre categorías |

### Proveedores `http://localhost:9090/proveedores`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /proveedores | Lista todos los proveedores |
| GET | /proveedores/activos | Lista proveedores activos |
| GET | /proveedores/{id} | Busca proveedor por ID |
| POST | /proveedores | Crea un proveedor |
| PUT | /proveedores/{id} | Actualiza un proveedor |
| DELETE | /proveedores/{id} | Elimina un proveedor |
| PATCH | /proveedores/{id}/desactivar | Desactiva un proveedor |
| PATCH | /proveedores/{id}/activar | Activa un proveedor |

### Reportes `http://localhost:9090/reportes`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /reportes | Lista todos los reportes generados |
| GET | /reportes/{id} | Busca reporte por ID |
| POST | /reportes | Crea un reporte manualmente |
| DELETE | /reportes/{id} | Elimina un reporte |
| GET | /reportes/generar/ventas?desde=...&hasta=... | Genera reporte de ventas por período |
| GET | /reportes/generar/stock-critico | Genera reporte de stock crítico |
| GET | /reportes/generar/pedidos | Genera reporte de estado de pedidos |

### Galería `http://localhost:9090/galeria`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /galeria | Lista todas las imágenes |
| GET | /galeria/{id} | Busca imagen por ID |
| GET | /galeria/producto/{productoId} | Imágenes de un producto |
| POST | /galeria | Sube una imagen |
| DELETE | /galeria/{id} | Elimina una imagen |

### Gastos `http://localhost:9090/gastos`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /gastos | Lista todos los gastos |
| GET | /gastos/{id} | Busca gasto por ID |
| POST | /gastos | Crea un gasto |
| PUT | /gastos/{id} | Actualiza un gasto |
| DELETE | /gastos/{id} | Elimina un gasto |
| GET | /gastos/categorias | Lista categorías de gasto |
| POST | /gastos/categorias | Crea categoría de gasto |

### Calendario `http://localhost:9090/calendario`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /calendario/produccion | Lista toda la producción |
| GET | /calendario/produccion/{id} | Busca producción por ID |
| GET | /calendario/produccion/calendario?desde=...&hasta=... | Producción en rango de fechas |
| POST | /calendario/produccion | Crea una orden de producción |
| PUT | /calendario/produccion/{id} | Actualiza producción |
| PATCH | /calendario/produccion/{id}/estado?nuevoEstado=... | Cambia estado de producción |
| DELETE | /calendario/produccion/{id} | Elimina producción |
| GET | /calendario/bloqueos | Lista bloqueos |
| POST | /calendario/bloqueos | Crea un bloqueo |
| DELETE | /calendario/bloqueos/{id} | Elimina un bloqueo |

---

## Datos de prueba

### Registrar un usuario
```json
POST http://localhost:9090/auth/registrar
{
  "nombreUsuario": "vendedor1",
  "contrasena": "123456",
  "correo": "vendedor@perlitas.cl",
  "roles": ["VENDEDOR"]
}
```

### Crear un material
```json
POST http://localhost:9090/materiales
{
  "nombre": "Hilo de seda",
  "descripcion": "Hilo fino para pulseras",
  "unidadMedida": "metros",
  "stockActual": 100,
  "stockMinimo": 10,
  "precioUnitario": 500
}
```

### Crear un producto
```json
POST http://localhost:9090/productos
{
  "nombre": "Pulsera trenzada",
  "descripcion": "Pulsera hecha a mano con hilo de seda",
  "precioVenta": 5000,
  "stockDisponible": 20,
  "activo": true
}
```

### Crear una venta
```json
POST http://localhost:9090/ventas
{
  "clienteId": 1,
  "metodoPago": "efectivo",
  "productos": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precioUnitario": 5000
    }
  ]
}
```

### Crear un pedido
```json
POST http://localhost:9090/pedidos
{
  "clienteId": 1,
  "descripcion": "Pulsera personalizada con nombre",
  "estado": "pendiente",
  "fechaEntrega": "2026-06-01",
  "total": 5000,
  "abonoPagado": 0
}
```

### Cambiar estado de un pedido
```
PATCH http://localhost:9090/pedidos/1/estado?nuevoEstado=en_fabricacion
```

Estados válidos: `pendiente` → `en_fabricacion` → `listo_para_entrega` → `entregado`

### Cambiar estado de una producción
```
PATCH http://localhost:9090/calendario/produccion/1/estado?nuevoEstado=en_proceso
```

Estados válidos: `programado` → `en_proceso` → `finalizado`. Se puede cancelar desde cualquier estado activo (`cancelado`).

### Crear una categoría
```json
POST http://localhost:9090/categorias
{
  "nombre": "Pulseras",
  "descripcion": "Todo tipo de pulseras",
  "activo": 1
}
```

### Generar reporte de ventas
```
GET http://localhost:9090/reportes/generar/ventas?desde=2026-01-01&hasta=2026-06-21
```

---

## Comunicación entre microservicios

| Servicio origen | Consulta a | Propósito |
|---|---|---|
| productos | materiales (8081) | Enriquecer productos con datos del material |
| ventas | productos (8082) | Enriquecer ventas con datos del producto vendido |
| pedidos | - | Servicio autónomo |
| reportes | todos los servicios | Consolidar datos para generar reportes |
| service_calendario | productos (8082), pedidos (8085) | Enriquecer producción con datos de producto y pedido |
| gastos | - | Servicio autónomo |
| galeria | - | Servicio autónomo |

El **Gateway** (puerto 9090) centraliza el acceso a todos los microservicios, valida tokens JWT y controla acceso por roles.

---

## Roles y permisos

| Rol | Permisos |
|---|---|
| ADMIN | Acceso completo a todos los endpoints |
| VENDEDOR | GET, POST, PUT - no puede eliminar recursos |
| CLIENTE | Solo GET (lectura) |

---

## Reglas de negocio

- Los pedidos solo pueden avanzar en orden: `pendiente` → `en_fabricacion` → `listo_para_entrega` → `entregado`. No se puede retroceder ni saltar estados.
- Las producciones siguen: `programado` → `en_proceso` → `finalizado`. Desde cualquier estado activo se puede pasar a `cancelado`.
- No se puede reasignar productos a una categoría inactiva.
- El total de una venta se calcula automáticamente en base a los productos y sus cantidades.
- La fecha de un pedido se asigna automáticamente al momento de su creación.
- No se puede programar producción si existe un bloqueo en el rango de fechas.
- Cada microservicio valida los tokens JWT recibidos del gateway antes de procesar la solicitud.

---

## Tests

Se implementaron tests unitarios con JUnit 5 y Mockito para el 60% de los microservicios:

| Microservicio | Service Tests | Controller Tests |
|---|---|---|---|
| materiales | ✅ | ✅ |
| productos | ✅ | ✅ |
| pedidos | ✅ | ✅ |
| categories | ✅ | ✅ |
| proveedores | ✅ | ✅ |
| ventas | ✅ | ✅ |
| gastos | ✅ | ✅ |
| galeria | ✅ | ✅ |
| service-auth | ✅ | ❌ |

Ejecutar tests:
```bash
cd Perlita/<microservicio>
./mvnw test
```

---

## Arquitectura

El proyecto sigue una arquitectura de microservicios con:

- **Patrón CSR** (Controller-Service-Repository) en cada microservicio
- **API Gateway** como punto de entrada único
- **Autenticación JWT** centralizada en el gateway
- **Comunicación síncrona** vía WebClient entre microservicios
- **Base de datos independiente** por microservicio (Database-per-Service)
- **Documentación OpenAPI/Swagger** expuesta a través del gateway
