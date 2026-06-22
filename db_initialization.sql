-- ============================================================
-- Perlitas de Eucarion - Inicialización de Bases de Datos
-- ============================================================
-- Ejecutar cada sección en la base de datos correspondiente.
-- Las tablas se crean automáticamente con ddl-auto=update,
-- este script es solo para datos de prueba iniciales.
-- ============================================================

-- ------------------------------------------------------------
-- 1. service_auth (db_service_auth)
-- ------------------------------------------------------------
INSERT INTO roles (nombre_rol) VALUES ('ADMIN');
INSERT INTO roles (nombre_rol) VALUES ('VENDEDOR');
INSERT INTO roles (nombre_rol) VALUES ('CLIENTE');

-- ------------------------------------------------------------
-- 2. materiales (db_materials)
-- ------------------------------------------------------------
INSERT INTO materiales (nombre, descripcion, unidad_medida, stock_actual, stock_minimo, precio_unitario)
VALUES ('Hilo encerado', 'Hilo encerado para pulseras y collares', 'metros', 500, 50, 200);
INSERT INTO materiales (nombre, descripcion, unidad_medida, stock_actual, stock_minimo, precio_unitario)
VALUES ('Mostacillas surtidas', 'Mostacillas de colores variados', 'gramos', 1000, 100, 50);
INSERT INTO materiales (nombre, descripcion, unidad_medida, stock_actual, stock_minimo, precio_unitario)
VALUES ('Cierre metálico', 'Cierres de metal dorado y plateado', 'unidad', 200, 20, 150);

-- ------------------------------------------------------------
-- 3. productos (db_products)
-- ------------------------------------------------------------
INSERT INTO productos (nombre, descripcion, precio_venta, stock_disponible, activo)
VALUES ('Pulsera artesanal', 'Pulsera tejida a mano con mostacillas', 5000, 20, 1);
INSERT INTO productos (nombre, descripcion, precio_venta, stock_disponible, activo)
VALUES ('Collar de mostacillas', 'Collar largo con mostacillas de colores', 8000, 15, 1);
INSERT INTO productos (nombre, descripcion, precio_venta, stock_disponible, activo)
VALUES ('Llavero personalizado', 'Llavero con diseño a elección', 3000, 30, 1);

-- ------------------------------------------------------------
-- 4. ventas (db_ventas)
-- ------------------------------------------------------------
INSERT INTO ventas (cliente_id, fecha, metodo_pago, total, comprobante_ref)
VALUES (1, CURRENT_DATE, 'efectivo', 13000, 'COMP-001');
INSERT INTO venta_producto (venta_id, producto_id, cantidad, precio_unitario)
VALUES (1, 1, 1, 5000);
INSERT INTO venta_producto (venta_id, producto_id, cantidad, precio_unitario)
VALUES (1, 2, 1, 8000);

-- ------------------------------------------------------------
-- 5. pedidos (db_orders)
-- ------------------------------------------------------------
INSERT INTO pedidos (cliente_id, descripcion, estado, fecha_pedido, fecha_entrega, total, abono_pagado)
VALUES (1, 'Pulsera personalizada con nombre "María"', 'pendiente', CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY), 6000, 2000);

-- ------------------------------------------------------------
-- 6. categories (db_categories)
-- ------------------------------------------------------------
INSERT INTO categorias (nombre, descripcion, activo) VALUES ('Pulseras', 'Todo tipo de pulseras artesanales', 1);
INSERT INTO categorias (nombre, descripcion, activo) VALUES ('Collares', 'Collares y gargantillas', 1);
INSERT INTO categorias (nombre, descripcion, activo) VALUES ('Llaveros', 'Llaveros personalizados', 1);

-- ------------------------------------------------------------
-- 7. proveedores (db_proveedores)
-- ------------------------------------------------------------
INSERT INTO proveedores (nombre, contacto, telefono, email, direccion, activo)
VALUES ('Distribuidora Artesanal Ltda.', 'Carlos Muñoz', '+56912345678', 'carlos@dartesanal.cl', 'Av. Siempre Viva 123', 1);

-- ------------------------------------------------------------
-- 8. gastos (db_gastos)
-- ------------------------------------------------------------
INSERT INTO categorias_gasto (nombre, descripcion) VALUES ('Insumos', 'Compra de materiales e insumos');
INSERT INTO categorias_gasto (nombre, descripcion) VALUES ('Servicios', 'Gastos de servicios básicos');
INSERT INTO gastos (categoria_id, descripcion, monto, fecha)
VALUES (1, 'Compra de hilo encerado', 15000, CURRENT_DATE);

-- ------------------------------------------------------------
-- 9. galeria (db_galeria)
-- ------------------------------------------------------------
INSERT INTO galeria (producto_id, url_imagen, descripcion, es_principal, fecha_subida, activo)
VALUES (1, 'https://ejemplo.com/imagenes/pulsera1.jpg', 'Pulsera artesanal color rojo', 1, CURRENT_DATE, 1);

-- ------------------------------------------------------------
-- 10. calendario (db_calendario)
-- ------------------------------------------------------------
INSERT INTO produccion (producto_id, fecha_inicio, fecha_fin_estimada, estado, notas)
VALUES (1, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), 'programado', 'Producción para reposición de stock');
INSERT INTO bloqueos (fecha_inicio, fecha_fin, motivo)
VALUES ('2026-12-24', '2026-12-31', 'Vacaciones de fin de año');

-- ------------------------------------------------------------
-- 11. reportes (db_reportes)
-- ------------------------------------------------------------
INSERT INTO reportes (tipo, fecha_generacion, parametros, resultado_json)
VALUES ('inicial', CURRENT_DATE, '{"origen":"setup"}', '{"mensaje":"Base de datos inicializada"}');
