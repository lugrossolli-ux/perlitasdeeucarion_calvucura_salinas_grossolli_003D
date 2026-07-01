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

El Gateway centraliza la documentación de todos los microservicios en **una sola interfaz** con un menú desplegable ("Select a definition") para cambiar entre servicios:

**http://localhost:9090/swagger-ui.html**

Internamente, cada microservicio expone su JSON de OpenAPI en una ruta propia, que el Gateway agrega:

| Microservicio | Ruta del JSON (api-docs) |
|---|---|
| service-auth | /api/v1/auth/v3/api-docs |
| materiales | /api/v1/materiales/v3/api-docs |
| productos | /api/v1/productos/v3/api-docs |
| ventas | /api/v1/ventas/v3/api-docs |
| categories | /api/v1/categorias/v3/api-docs |
| pedidos | /api/v1/pedidos/v3/api-docs |
| proveedores | /api/v1/proveedores/v3/api-docs |
| reportes | /api/v1/reportes/v3/api-docs |
| galeria | /api/v1/galeria/v3/api-docs |
| gastos | /api/v1/gastos/v3/api-docs |
| service_calendario | /api/v1/calendario/v3/api-docs |

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

### Pedido-Materiales `http://localhost:9090/pedido-materiales`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /pedido-materiales/pedido/{pedidoId} | Materiales reservados de un pedido |
| POST | /pedido-materiales | Reserva un material para un pedido |
| DELETE | /pedido-materiales/{id} | Elimina una reserva |

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

### Proveedor-Materiales `http://localhost:9090/proveedor-materiales`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /proveedor-materiales/proveedor/{proveedorId} | Materiales de un proveedor |
| GET | /proveedor-materiales/material/{materialId} | Proveedores de un material |
| POST | /proveedor-materiales | Crea relación proveedor-material |
| PUT | /proveedor-materiales/{id} | Actualiza relación |
| DELETE | /proveedor-materiales/{id} | Elimina relación |

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
| GET | /gastos/categoria/{categoriaId} | Gastos por categoría |
| GET | /gastos/proveedor/{proveedorId} | Gastos por proveedor |
| POST | /gastos | Crea un gasto |
| PUT | /gastos/{id} | Actualiza un gasto |
| DELETE | /gastos/{id} | Elimina un gasto |

### Categorías de gasto `http://localhost:9090/categorias-gasto`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /categorias-gasto | Lista categorías de gasto |
| GET | /categorias-gasto/{id} | Busca categoría de gasto por ID |
| POST | /categorias-gasto | Crea categoría de gasto |
| PUT | /categorias-gasto/{id} | Actualiza categoría de gasto |
| DELETE | /categorias-gasto/{id} | Elimina categoría de gasto |

### Calendario / Producción `http://localhost:9090/api/v1/produccion`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/v1/produccion | Lista toda la producción |
| GET | /api/v1/produccion/{id} | Busca producción por ID |
| GET | /api/v1/produccion/calendario?desde=...&hasta=... | Producción en rango de fechas |
| POST | /api/v1/produccion | Crea una orden de producción |
| PUT | /api/v1/produccion/{id} | Actualiza producción |
| PATCH | /api/v1/produccion/{id}/estado?nuevoEstado=... | Cambia estado de producción |
| DELETE | /api/v1/produccion/{id} | Elimina producción |
| GET | /api/v1/produccion/bloqueos | Lista bloqueos |
| POST | /api/v1/produccion/bloqueos | Crea un bloqueo |
| DELETE | /api/v1/produccion/bloqueos/{id} | Elimina un bloqueo |

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
⚠️ Por el bug #1 y #2 descritos arriba, este endpoint actualmente no logra consultar a `ventas` ni a `productos` correctamente, por lo que el reporte generado queda con conteos en `0`.

---

## Comunicación entre microservicios

| Servicio origen | Consulta a | Propósito |
|---|---|---|
| productos | materiales (8081) | Enriquecer productos con datos del material |
| ventas | productos (8082) | Enriquecer ventas con datos del producto vendido |
| pedidos | productos (8082) | Verifica que el producto exista antes de crear el pedido |
| gastos | proveedores (8086) | Enriquecer gastos con datos del proveedor |
| reportes | productos, materiales, ventas, pedidos, gastos | Consolidar datos para generar reportes ⚠️ ver bugs conocidos |
| service_calendario | productos (8082), pedidos (8085) | Enriquecer producción con datos de producto y pedido ⚠️ ver bugs conocidos |
| galeria | - | Servicio autónomo |
| categories | - | Servicio autónomo |

El **Gateway** (puerto 9090) centraliza el acceso a todos los microservicios, valida tokens JWT y controla acceso por roles.

---

## ⚠️ Bugs conocidos

Estos problemas existen actualmente en el código y están pendientes de corrección:

1. **`reportes` llama a rutas equivocadas en los demás servicios.** Usa prefijos `/api/v1/...` (`/api/v1/materiales/stock-bajo`, `/api/v1/productos`, `/api/v1/ventas`, `/api/v1/pedidos`) que no existen en los controladores reales (que no tienen ese prefijo, ej. `/materiales/stock-bajo`, `/productos`). Como las llamadas fallan en silencio (`onErrorResume`), los reportes generados (`/reportes/generar/...`) siempre muestran conteos en `0`.
2. **`reportes` apunta al puerto equivocado para pedidos.** En `application.properties`, `microservicio.pedidos.url=http://localhost:8084` (el puerto de `categories`), debería ser `8085`.
3. **`service_calendario` también usa rutas con prefijo `/api/v1/` al llamar a `productos` y `pedidos`** (`/api/v1/productos/{id}`, `/api/v1/pedidos/{id}`), pero esos controladores no tienen ese prefijo. Por esto, `datosProducto` y `datosPedido` siempre salen `null` al consultar una producción, aunque los puertos configurados (8082 y 8085) sí son correctos.
4. **`materiales` — endpoint de stock bajo no funciona como se espera.** `GET /materiales/stock-bajo` filtra `stockActual < 0`, pero debería comparar `stockActual < stockMinimo`. Como el stock normalmente nunca es negativo, este endpoint nunca devuelve resultados útiles.

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