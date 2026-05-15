DROP TABLE IF EXISTS bloqueos;
DROP TABLE IF EXISTS produccion;
DROP TABLE IF EXISTS producto_categoria;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS galeria_etiqueta;
DROP TABLE IF EXISTS etiquetas;
DROP TABLE IF EXISTS galeria;
DROP TABLE IF EXISTS gastos;
DROP TABLE IF EXISTS categorias_gasto;
DROP TABLE IF EXISTS proveedor_material;
DROP TABLE IF EXISTS proveedores;
DROP TABLE IF EXISTS reportes;
DROP TABLE IF EXISTS pedido_material;
DROP TABLE IF EXISTS venta_producto;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS producto_material;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS materiales;
DROP TABLE IF EXISTS clientes;


CREATE TABLE clientes (
    id        INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(150)    NOT NULL,
    telefono  VARCHAR(20),
    email     VARCHAR(100)
);

CREATE TABLE materiales (
    id                  INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(150)    NOT NULL,
    descripcion         VARCHAR(4000),
    unidad_medida       VARCHAR(50)     NOT NULL,
    stock_actual        DECIMAL(10,2)   NOT NULL DEFAULT 0,
    stock_minimo        DECIMAL(10,2)   NOT NULL DEFAULT 0,
    precio_unitario     DECIMAL(10,2)   NOT NULL,
    fecha_actualizacion DATE            NOT NULL DEFAULT (CURRENT_DATE)
);

CREATE TABLE productos (
    id               INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(150)    NOT NULL,
    descripcion      VARCHAR(4000),
    precio_venta     DECIMAL(10,2)   NOT NULL,
    stock_disponible INT             NOT NULL DEFAULT 0,
    activo           TINYINT(1)      NOT NULL DEFAULT 1
);

CREATE TABLE producto_material (
    id                 INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    producto_id        INT             NOT NULL,
    material_id        INT             NOT NULL,
    cantidad_requerida DECIMAL(10,2)   NOT NULL,
    CONSTRAINT fk_pm_producto  FOREIGN KEY (producto_id)  REFERENCES productos(id),
    CONSTRAINT fk_pm_material  FOREIGN KEY (material_id)  REFERENCES materiales(id)
);

CREATE TABLE pedidos (
    id              INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cliente_id      INT             NOT NULL,
    descripcion     VARCHAR(4000)   NOT NULL,
    estado          VARCHAR(50)     NOT NULL DEFAULT 'pendiente',
    fecha_pedido    DATE            NOT NULL DEFAULT (CURRENT_DATE),
    fecha_entrega   DATE,
    total           DECIMAL(10,2)   NOT NULL,
    abono_pagado    DECIMAL(10,2)   NOT NULL DEFAULT 0,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE ventas (
    id              INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cliente_id      INT             NOT NULL,
    pedido_id       INT             DEFAULT NULL,
    fecha           DATE            NOT NULL DEFAULT (CURRENT_DATE),
    metodo_pago     VARCHAR(50)     NOT NULL,
    total           DECIMAL(10,2)   NOT NULL,
    comprobante_ref VARCHAR(100),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_venta_pedido  FOREIGN KEY (pedido_id)  REFERENCES pedidos(id)
);

CREATE TABLE venta_producto (
    id              INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    venta_id        INT             NOT NULL,
    producto_id     INT             NOT NULL,
    cantidad        INT             NOT NULL,
    precio_unitario DECIMAL(10,2)   NOT NULL,
    CONSTRAINT fk_vp_venta    FOREIGN KEY (venta_id)   REFERENCES ventas(id),
    CONSTRAINT fk_vp_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE pedido_material (
    id                 INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pedido_id          INT             NOT NULL,
    material_id        INT             NOT NULL,
    cantidad_reservada DECIMAL(10,2)   NOT NULL,
    CONSTRAINT fk_pedmat_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedidos(id),
    CONSTRAINT fk_pedmat_material FOREIGN KEY (material_id) REFERENCES materiales(id)
);

CREATE TABLE reportes (
    id               INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipo             VARCHAR(100)    NOT NULL,
    fecha_generacion DATE            NOT NULL DEFAULT (CURRENT_DATE),
    parametros       TEXT,
    resultado_json   TEXT            NOT NULL
);

CREATE TABLE proveedores (
    id          INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(150)    NOT NULL,
    contacto    VARCHAR(150),
    telefono    VARCHAR(20),
    email       VARCHAR(100),
    activo      TINYINT(1)      NOT NULL DEFAULT 1
);

CREATE TABLE proveedor_material (
    id                  INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    proveedor_id        INT             NOT NULL,
    material_id         INT             NOT NULL,
    precio_proveedor    DECIMAL(10,2)   NOT NULL,
    fecha_actualizacion DATE            NOT NULL DEFAULT (CURRENT_DATE),
    CONSTRAINT fk_provmat_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id),
    CONSTRAINT fk_provmat_material  FOREIGN KEY (material_id)  REFERENCES materiales(id)
);

CREATE TABLE categorias_gasto (
    id          INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100)    NOT NULL,
    descripcion VARCHAR(300)
);

CREATE TABLE gastos (
    id              INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    categoria_id    INT             NOT NULL,
    descripcion     VARCHAR(300)    NOT NULL,
    monto           DECIMAL(10,2)   NOT NULL,
    fecha           DATE            NOT NULL DEFAULT (CURRENT_DATE),
    comprobante_ref VARCHAR(100),
    proveedor_id    INT             DEFAULT NULL,
    CONSTRAINT fk_gasto_cat       FOREIGN KEY (categoria_id) REFERENCES categorias_gasto(id),
    CONSTRAINT fk_gasto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
);

CREATE TABLE galeria (
    id           INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    producto_id  INT             NOT NULL,
    url_imagen   VARCHAR(500)    NOT NULL,
    descripcion  VARCHAR(300),
    es_principal TINYINT(1)      NOT NULL DEFAULT 0,
    fecha_subida DATE            NOT NULL DEFAULT (CURRENT_DATE),
    activo       TINYINT(1)      NOT NULL DEFAULT 1,
    CONSTRAINT fk_galeria_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE etiquetas (
    id      INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100)    NOT NULL
);

CREATE TABLE galeria_etiqueta (
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    galeria_id  INT NOT NULL,
    etiqueta_id INT NOT NULL,
    CONSTRAINT fk_ge_galeria  FOREIGN KEY (galeria_id)  REFERENCES galeria(id),
    CONSTRAINT fk_ge_etiqueta FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(id)
);

CREATE TABLE categorias (
    id          INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100)    NOT NULL,
    descripcion VARCHAR(300),
    activo      TINYINT(1)      NOT NULL DEFAULT 1
);

CREATE TABLE producto_categoria (
    id           INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    producto_id  INT NOT NULL,
    categoria_id INT NOT NULL,
    CONSTRAINT fk_pc_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_pc_producto  FOREIGN KEY (producto_id)  REFERENCES productos(id)
);

CREATE TABLE produccion (
    id                 INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pedido_id          INT             DEFAULT NULL,
    producto_id        INT             NOT NULL,
    fecha_inicio       DATE            NOT NULL,
    fecha_fin_estimada DATE            NOT NULL,
    fecha_fin_real     DATE            DEFAULT NULL,
    estado             VARCHAR(50)     NOT NULL DEFAULT 'programado',
    notas              VARCHAR(4000),
    CONSTRAINT fk_prod_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedidos(id),
    CONSTRAINT fk_prod_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE bloqueos (
    id           INT             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE            NOT NULL,
    fecha_fin    DATE            NOT NULL,
    motivo       VARCHAR(300)    NOT NULL
);
