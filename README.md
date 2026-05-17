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
| materiales | 8081 | db_materials |
| productos | 8082 | db_products |
| ventas | 8083 | db_ventas |
| categorias | 8084 | db_categories |
| pedidos | 8085 | db_orders |
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

### 3. Ejecutar cada microservicio
Abrir una terminal por cada microservicio y correr:

```bash
cd Perlita/materiales
./mvnw spring-boot:run
```
```bash
cd Perlita/productos
./mvnw spring-boot:run
```
```bash
cd Perlita/ventas
./mvnw spring-boot:run
```
```bash
cd Perlita/categories
./mvnw spring-boot:run
```
```bash
cd Perlita/pedidos
./mvnw spring-boot:run
```
```bash
cd Perlita/gateway
./mvnw spring-boot:run
```

Las bases de datos se crean automáticamente al iniciar cada microservicio.

---

## Endpoints disponibles

Todos los endpoints pueden consumirse directamente por su puerto o a través del Gateway en `http://localhost:9090`.

### Materiales `http://localhost:8081/materiales`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /materiales | Lista todos los materiales |
| GET | /materiales/{id} | Busca material por ID |
| GET | /materiales/stock-bajo | Lista materiales con stock bajo |
| POST | /materiales | Crea un material |
| PUT | /materiales/{id} | Actualiza un material |
| DELETE | /materiales/{id} | Elimina un material |

### Productos `http://localhost:8082/productos`

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

### Ventas `http://localhost:8083/ventas`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /ventas | Lista todas las ventas |
| GET | /ventas/{id} | Busca venta por ID |
| GET | /ventas/cliente/{clienteId} | Ventas por cliente |
| GET | /ventas/resumen/metodo-pago | Resumen por método de pago |
| POST | /ventas | Crea una venta |

### Categorías `http://localhost:8084/categorias`

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

### Pedidos `http://localhost:8085/pedidos`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /pedidos | Lista todos los pedidos |
| GET | /pedidos/{id} | Busca pedido por ID |
| GET | /pedidos/estado/{estado} | Pedidos por estado |
| GET | /pedidos/cliente/{clienteId} | Pedidos por cliente |
| POST | /pedidos | Crea un pedido |
| PUT | /pedidos/{id} | Actualiza un pedido |
| DELETE | /pedidos/{id} | Elimina un pedido |
| PATCH | /pedidos/{id}/estado | Cambia estado del pedido |

---

## Datos de prueba

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

PATCH http://localhost:9090/pedidos/1/estado?nuevoEstado=en_proceso

Los estados válidos son: `pendiente` → `en_proceso` → `completado`

### Crear una categoría
```json
POST http://localhost:9090/categorias
{
  "nombre": "Pulseras",
  "descripcion": "Todo tipo de pulseras",
  "activo": 1
}
```

---

## Comunicación entre microservicios

- **Productos** consulta a **Materiales** (puerto 8081) para enriquecer la información de cada producto con los datos del material asociado.
- **Ventas** consulta a **Productos** (puerto 8082) para enriquecer cada venta con los datos del producto vendido.
- El **Gateway** (puerto 9090) centraliza el acceso a todos los microservicios.

---

## Reglas de negocio

- Los pedidos solo pueden avanzar en orden: `pendiente` → `en_proceso` → `completado`. No se puede retroceder ni saltar estados.
- No se puede reasignar productos a una categoría inactiva.
- El total de una venta se calcula automáticamente en base a los productos y sus cantidades.
- La fecha de un pedido se asigna automáticamente al momento de su creación.