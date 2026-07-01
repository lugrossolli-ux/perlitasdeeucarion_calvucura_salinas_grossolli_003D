-- =====================================================================
--  DATOS DE PRUEBA - PROYECTO PERLITA (microservicios)
-- =====================================================================

-- =====================================================================
-- 1) db_seguridad_perlita  (service-auth, puerto 8091)
-- =====================================================================
USE db_seguridad_perlita;

INSERT INTO usuarios (nombre_usuario, contrasena, correo)
SELECT 'cliente1', '$2b$10$Z0BZoshGUFA4AnWFYZakFO/ij41ZbQvVzFgdoXpm0Y3Y8GYN8ji.O', 'cliente1@perlitas.cl'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre_usuario = 'cliente1');

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id FROM usuarios u, roles r
WHERE u.nombre_usuario = 'cliente1' AND r.nombre_rol = 'CLIENTE'
  AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
  );

-- admin/admin123 y vendedor/vendedor123 ya los crea el seeder de la app.
-- cliente1/cliente123 lo agrega este script.


-- =====================================================================
-- 2) db_materials  (materiales, puerto 8081)
-- =====================================================================
USE db_materials;

INSERT INTO materiales (id, nombre, descripcion, unidad_medida, stock_actual, stock_minimo, precio_unitario, fecha_actualizacion) VALUES
(1, 'Hilo de nylon',            'Hilo resistente para enhebrado de collares y pulseras', 'metro',  150.0, 20.0,  50.0,  '2026-06-01'),
(2, 'Cuentas de vidrio 8mm',    'Cuentas de vidrio checo de colores surtidos',           'unidad', 800.0, 100.0, 25.0,  '2026-06-02'),
(3, 'Perlas de agua dulce',     'Perlas naturales cultivadas, tono blanco/crema',        'unidad', 300.0, 50.0,  180.0, '2026-06-02'),
(4, 'Cierres de acero',         'Cierres tipo mosquetón de acero inoxidable',             'unidad', 200.0, 30.0,  120.0, '2026-06-03'),
(5, 'Cristal Swarovski 6mm',    'Cristales facetados de alta calidad',                   'unidad', 400.0, 60.0,  350.0, '2026-06-03'),
(6, 'Alambre de bisutería',     'Alambre flexible calibre 0.6mm',                        'metro',  100.0, 15.0,  90.0,  '2026-06-04'),
(7, 'Dijes metálicos',          'Dijes decorativos bañados en oro',                      'unidad', 250.0, 40.0,  200.0, '2026-06-05'),
(8, 'Cuero sintético 3mm',      'Cordón de cuero sintético para pulseras',               'metro',  80.0,  10.0,  60.0,  '2026-06-05');


-- =====================================================================
-- 3) db_products  (productos, puerto 8082)
-- =====================================================================
USE db_products;

INSERT INTO productos (id, nombre, descripcion, precio_venta, stock_disponible, activo) VALUES
(1, 'Collar Perlas Clásico',   'Collar de perlas de agua dulce con cierre de acero', 15990.0, 12, 1),
(2, 'Pulsera Cristal Swarovski','Pulsera elástica con cristales Swarovski',            9990.0,  20, 1),
(3, 'Aretes Dorados',          'Aretes colgantes con dijes bañados en oro',           7990.0,  25, 1),
(4, 'Choker Bohemio',          'Choker corto de cuentas de vidrio estilo boho',       6990.0,  15, 1),
(5, 'Collar Multihilo',        'Collar de varios hilos de cuentas de colores',         12990.0, 10, 1),
(6, 'Pulsera Ajustable Cuero', 'Pulsera de cuero sintético con dije metálico',         8990.0,  18, 0);

INSERT INTO producto_material (producto_id, material_id, cantidad_requerida) VALUES
(1, 3, 24.0),
(1, 1, 0.6),
(1, 4, 1.0),
(2, 5, 18.0),
(2, 6, 0.3),
(3, 7, 2.0),
(3, 4, 2.0),
(4, 2, 30.0),
(4, 1, 0.5),
(5, 2, 40.0),
(5, 3, 10.0),
(6, 8, 0.4),
(6, 7, 1.0);


-- =====================================================================
-- 4) db_categories  (categories, puerto 8084)
-- =====================================================================
USE db_categories;

INSERT INTO categorias (id, nombre, descripcion, activo) VALUES
(1, 'Collares',            'Collares de todo tipo',                     1),
(2, 'Pulseras',            'Pulseras y brazaletes',                     1),
(3, 'Aretes',              'Aretes y pendientes',                       1),
(4, 'Chokers',             'Chokers y gargantillas cortas',            1),
(5, 'Ediciones especiales','Productos de temporada o edición limitada', 1);

INSERT INTO producto_categoria (producto_id, categoria_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 1),
(6, 2),
(1, 5);


-- =====================================================================
-- 5) db_suppliers  (proveedores, puerto 8086)
-- =====================================================================
USE db_suppliers;

INSERT INTO proveedores (id, nombre, contacto, telefono, email, direccion, activo) VALUES
(1, 'Distribuidora Andina',  'Marcela Rojas',  '+56912345678', 'contacto@andina.cl',    'Av. Providencia 1234, Santiago', 1),
(2, 'Cristales del Sur',     'Pedro Muñoz',    '+56923456789', 'ventas@cristalsur.cl',  'Camino a Concepción 500, Talca', 1),
(3, 'ImportBisutería Ltda.', 'Camila Torres',  '+56934567890', 'info@importbisu.cl',    'Barrio Meiggs 88, Santiago',     1),
(4, 'Perlas y Más',          'Jorge Herrera',  '+56945678901', 'jorge@perlasymas.cl',   'Av. Alemana 210, Temuco',        1);

INSERT INTO proveedor_material (proveedor_id, material_id, precio_proveedor, fecha_actualizacion) VALUES
(1, 1, 35.00,  '2026-05-20'),
(1, 2, 18.00,  '2026-05-20'),
(2, 5, 280.00, '2026-05-22'),
(2, 6, 70.00,  '2026-05-22'),
(3, 4, 90.00,  '2026-05-25'),
(3, 7, 150.00, '2026-05-25'),
(4, 3, 140.00, '2026-05-28'),
(4, 8, 45.00,  '2026-05-28');


-- =====================================================================
-- 6) db_orders  (pedidos, puerto 8085)
-- =====================================================================
USE db_orders;

INSERT INTO pedidos (id, cliente_id, descripcion, estado, fecha_pedido, fecha_entrega, total, abono_pagado) VALUES
(1, 101, 'Collar personalizado para regalo de aniversario', 'entregado',   '2026-06-01', '2026-06-05', 15990.00, 15990.00),
(2, 102, 'Set de pulsera y aretes a juego',                  'en_proceso',  '2026-06-10', '2026-06-18', 17980.00, 9000.00),
(3, 103, 'Choker boho para evento',                          'pendiente',   '2026-06-20', NULL,         6990.00,  3000.00),
(4, 104, 'Collar multihilo edición especial',                'en_proceso',  '2026-06-22', '2026-06-30', 12990.00, 0.00),
(5, 105, 'Pedido cancelado por el cliente',                  'cancelado',   '2026-06-15', NULL,         8990.00,  0.00);

INSERT INTO pedido_material (pedido_id, material_id, cantidad_reservada) VALUES
(1, 3, 24.0),
(1, 4, 1.0),
(2, 5, 18.0),
(2, 7, 2.0),
(3, 2, 30.0),
(4, 2, 40.0),
(4, 3, 10.0);


-- =====================================================================
-- 7) db_ventas  (ventas, puerto 8083)
-- =====================================================================
USE db_ventas;

INSERT INTO ventas (id, cliente_id, pedido_id, metodo_pago, total, comprobante_ref, fecha) VALUES
(1, 101, 1,    'transferencia', 15990.0, 'BOL-0001', '2026-06-05'),
(2, 106, NULL, 'efectivo',      9990.0,  'BOL-0002', '2026-06-08'),
(3, 107, NULL, 'tarjeta',       7990.0,  'BOL-0003', '2026-06-12'),
(4, 108, NULL, 'efectivo',      6990.0,  'BOL-0004', '2026-06-14'),
(5, 109, NULL, 'transferencia', 21980.0, 'BOL-0005', '2026-06-19'),
(6, 110, NULL, 'tarjeta',       12990.0, 'BOL-0006', '2026-06-25');

INSERT INTO venta_producto (venta_id, producto_id, cantidad, precio_unitario) VALUES
(1, 1, 1, 15990.0),
(2, 2, 1, 9990.0),
(3, 3, 1, 7990.0),
(4, 4, 1, 6990.0),
(5, 2, 1, 9990.0),
(5, 3, 1, 7990.0),
(5, 4, 1, 3990.0),
(6, 5, 1, 12990.0);


-- =====================================================================
-- 8) db_expenses  (gastos, puerto 8089)
-- =====================================================================
USE db_expenses;

INSERT INTO categorias_gasto (id, nombre, descripcion) VALUES
(1, 'Materiales',  'Compra de insumos y materia prima'),
(2, 'Transporte',  'Envíos, fletes y combustible'),
(3, 'Marketing',   'Publicidad y redes sociales'),
(4, 'Servicios',   'Cuentas, arriendo y servicios básicos');

INSERT INTO gastos (categoria_id, descripcion, monto, fecha, comprobante_ref, proveedor_id) VALUES
(1, 'Compra de cristales Swarovski',        95000.0, '2026-05-22', 'FAC-1001', 2),
(1, 'Compra de hilo y cuentas de vidrio',   42000.0, '2026-05-20', 'FAC-1002', 1),
(2, 'Envío de pedido a regiones',            8500.0, '2026-06-06', 'FAC-1003', NULL),
(3, 'Campaña en Instagram',                 30000.0, '2026-06-10', 'FAC-1004', NULL),
(4, 'Pago de arriendo del taller',         180000.0, '2026-06-01', 'FAC-1005', NULL);


-- =====================================================================
-- 9) db_galeria  (galeria, puerto 8088)
-- =====================================================================
USE db_galeria;

INSERT INTO galeria (producto_id, url_imagen, descripcion, es_principal, fecha_subida, activo) VALUES
(1, 'https://picsum.photos/seed/collar-perlas-1/600/600', 'Foto principal collar de perlas',     1, '2026-06-01', 1),
(1, 'https://picsum.photos/seed/collar-perlas-2/600/600', 'Detalle del cierre',                    0, '2026-06-01', 1),
(2, 'https://picsum.photos/seed/pulsera-cristal-1/600/600','Foto principal pulsera Swarovski',    1, '2026-06-02', 1),
(3, 'https://picsum.photos/seed/aretes-dorados-1/600/600', 'Foto principal aretes dorados',       1, '2026-06-03', 1),
(4, 'https://picsum.photos/seed/choker-boho-1/600/600',    'Foto principal choker bohemio',       1, '2026-06-04', 1),
(5, 'https://picsum.photos/seed/collar-multihilo-1/600/600','Foto principal collar multihilo',    1, '2026-06-05', 1);


-- =====================================================================
-- 10) db_reportes  (reportes, puerto 8087)
-- =====================================================================
USE db_reportes;

INSERT INTO reportes (tipo, fecha_generacion, parametros, resultado_json) VALUES
('ventas_mensuales', '2026-06-30', '{"mes":"2026-06"}',
 '{"totalVentas":6,"montoTotal":66040,"productoMasVendido":"Aretes Dorados"}'),
('stock_bajo', '2026-06-30', '{"umbral":"stock_minimo"}',
 '{"materialesBajoStock":["Alambre de bisutería"]}'),
('gastos_mensuales', '2026-06-30', '{"mes":"2026-06"}',
 '{"totalGastos":355500,"categoriaMayorGasto":"Servicios"}');


-- =====================================================================
-- 11) db_calendario  (service_calendario, puerto 8090)
-- =====================================================================
USE db_calendario;

INSERT INTO bloqueos (fecha_inicio, fecha_fin, motivo) VALUES
('2026-07-18', '2026-07-20', 'Fiestas Patrias - taller cerrado'),
('2026-12-24', '2026-12-31', 'Vacaciones de fin de año');

INSERT INTO produccion (pedido_id, producto_id, fecha_inicio, fecha_fin_estimada, fecha_fin_real, estado, notas) VALUES
(1, 1, '2026-06-02', '2026-06-04', '2026-06-04', 'finalizado',  'Entregado a tiempo'),
(2, 2, '2026-06-11', '2026-06-16', NULL,          'en_proceso',  'Falta ensamblar cierre'),
(4, 5, '2026-06-23', '2026-06-29', NULL,          'programado',  'Pendiente de inicio'),
(NULL, 6, '2026-07-01', '2026-07-05', NULL,       'programado',  'Producción de stock, sin pedido asociado');
