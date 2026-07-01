DROP TABLE bloqueos CASCADE CONSTRAINTS;
DROP TABLE produccion CASCADE CONSTRAINTS;
DROP TABLE producto_categoria CASCADE CONSTRAINTS;
DROP TABLE categorias CASCADE CONSTRAINTS;
DROP TABLE galeria_etiqueta CASCADE CONSTRAINTS;
DROP TABLE etiquetas CASCADE CONSTRAINTS;
DROP TABLE galeria CASCADE CONSTRAINTS;
DROP TABLE gastos CASCADE CONSTRAINTS;
DROP TABLE categorias_gasto CASCADE CONSTRAINTS;
DROP TABLE proveedor_material CASCADE CONSTRAINTS;
DROP TABLE proveedores CASCADE CONSTRAINTS;
DROP TABLE reportes CASCADE CONSTRAINTS;
DROP TABLE pedido_material CASCADE CONSTRAINTS;
DROP TABLE venta_producto CASCADE CONSTRAINTS;
DROP TABLE ventas CASCADE CONSTRAINTS;
DROP TABLE pedidos CASCADE CONSTRAINTS;
DROP TABLE producto_material CASCADE CONSTRAINTS;
DROP TABLE productos CASCADE CONSTRAINTS;
DROP TABLE materiales CASCADE CONSTRAINTS;
DROP TABLE clientes CASCADE CONSTRAINTS;


CREATE TABLE clientes (
    id        NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre    VARCHAR2(150)   NOT NULL,
    telefono  VARCHAR2(20),
    email     VARCHAR2(100)
);

CREATE TABLE materiales (
    id                  NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre              VARCHAR2(150)   NOT NULL,
    descripcion         VARCHAR2(4000),
    unidad_medida       VARCHAR2(50)    NOT NULL,
    stock_actual        NUMBER(10,2)    DEFAULT 0 NOT NULL,
    stock_minimo        NUMBER(10,2)    DEFAULT 0 NOT NULL,
    precio_unitario     NUMBER(10,2)    NOT NULL,
    fecha_actualizacion DATE            DEFAULT SYSDATE NOT NULL
);

CREATE TABLE productos (
    id               NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre           VARCHAR2(150)   NOT NULL,
    descripcion      VARCHAR2(4000),
    precio_venta     NUMBER(10,2)    NOT NULL,
    stock_disponible NUMBER(10)      DEFAULT 0 NOT NULL,
    activo           NUMBER(1)       DEFAULT 1 NOT NULL
);

CREATE TABLE producto_material (
    id                 NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id        NUMBER          NOT NULL,
    material_id        NUMBER          NOT NULL,
    cantidad_requerida NUMBER(10,2)    NOT NULL,
    CONSTRAINT fk_pm_producto  FOREIGN KEY (producto_id)  REFERENCES productos(id),
    CONSTRAINT fk_pm_material  FOREIGN KEY (material_id)  REFERENCES materiales(id)
);

CREATE TABLE pedidos (
    id              NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cliente_id      NUMBER          NOT NULL,
    descripcion     VARCHAR2(4000)  NOT NULL,
    estado          VARCHAR2(50)    DEFAULT 'pendiente' NOT NULL,
    fecha_pedido    DATE            DEFAULT SYSDATE NOT NULL,
    fecha_entrega   DATE,
    total           NUMBER(10,2)    NOT NULL,
    abono_pagado    NUMBER(10,2)    DEFAULT 0 NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE ventas (
    id              NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cliente_id      NUMBER          NOT NULL,
    pedido_id       NUMBER          DEFAULT NULL,
    fecha           DATE            DEFAULT SYSDATE NOT NULL,
    metodo_pago     VARCHAR2(50)    NOT NULL,
    total           NUMBER(10,2)    NOT NULL,
    comprobante_ref VARCHAR2(100),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_venta_pedido  FOREIGN KEY (pedido_id)  REFERENCES pedidos(id)
);

CREATE TABLE venta_producto (
    id              NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    venta_id        NUMBER          NOT NULL,
    producto_id     NUMBER          NOT NULL,
    cantidad        NUMBER(10)      NOT NULL,
    precio_unitario NUMBER(10,2)    NOT NULL,
    CONSTRAINT fk_vp_venta    FOREIGN KEY (venta_id)    REFERENCES ventas(id),
    CONSTRAINT fk_vp_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE pedido_material (
    id                 NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pedido_id          NUMBER          NOT NULL,
    material_id        NUMBER          NOT NULL,
    cantidad_reservada NUMBER(10,2)    NOT NULL,
    CONSTRAINT fk_pedmat_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedidos(id),
    CONSTRAINT fk_pedmat_material FOREIGN KEY (material_id) REFERENCES materiales(id)
);

CREATE TABLE reportes (
    id               NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo             VARCHAR2(100)   NOT NULL,
    fecha_generacion DATE            DEFAULT SYSDATE NOT NULL,
    parametros       CLOB,
    resultado_json   CLOB            NOT NULL
);

CREATE TABLE proveedores (
    id          NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR2(150)   NOT NULL,
    contacto    VARCHAR2(150),
    telefono    VARCHAR2(20),
    email       VARCHAR2(100),
    activo      NUMBER(1)       DEFAULT 1 NOT NULL
);

CREATE TABLE proveedor_material (
    id                  NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    proveedor_id        NUMBER          NOT NULL,
    material_id         NUMBER          NOT NULL,
    precio_proveedor    NUMBER(10,2)    NOT NULL,
    fecha_actualizacion DATE            DEFAULT SYSDATE NOT NULL,
    CONSTRAINT fk_provmat_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id),
    CONSTRAINT fk_provmat_material  FOREIGN KEY (material_id)  REFERENCES materiales(id)
);

CREATE TABLE categorias_gasto (
    id          NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR2(100)   NOT NULL,
    descripcion VARCHAR2(300)
);

CREATE TABLE gastos (
    id              NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    categoria_id    NUMBER          NOT NULL,
    descripcion     VARCHAR2(300)   NOT NULL,
    monto           NUMBER(10,2)    NOT NULL,
    fecha           DATE            DEFAULT SYSDATE NOT NULL,
    comprobante_ref VARCHAR2(100),
    proveedor_id    NUMBER          DEFAULT NULL,
    CONSTRAINT fk_gasto_cat       FOREIGN KEY (categoria_id) REFERENCES categorias_gasto(id),
    CONSTRAINT fk_gasto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
);

CREATE TABLE galeria (
    id           NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id  NUMBER          NOT NULL,
    url_imagen   VARCHAR2(500)   NOT NULL,
    descripcion  VARCHAR2(300),
    es_principal NUMBER(1)       DEFAULT 0 NOT NULL,
    fecha_subida DATE            DEFAULT SYSDATE NOT NULL,
    activo       NUMBER(1)       DEFAULT 1 NOT NULL,
    CONSTRAINT fk_galeria_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE etiquetas (
    id      NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre  VARCHAR2(100)   NOT NULL
);

CREATE TABLE galeria_etiqueta (
    id          NUMBER  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    galeria_id  NUMBER  NOT NULL,
    etiqueta_id NUMBER  NOT NULL,
    CONSTRAINT fk_ge_galeria  FOREIGN KEY (galeria_id)  REFERENCES galeria(id),
    CONSTRAINT fk_ge_etiqueta FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(id)
);

CREATE TABLE categorias (
    id          NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR2(100)   NOT NULL,
    descripcion VARCHAR2(300),
    activo      NUMBER(1)       DEFAULT 1 NOT NULL
);

CREATE TABLE producto_categoria (
    id           NUMBER  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id  NUMBER  NOT NULL,
    categoria_id NUMBER  NOT NULL,
    CONSTRAINT fk_pc_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_pc_producto  FOREIGN KEY (producto_id)  REFERENCES productos(id)
);

CREATE TABLE produccion (
    id                 NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pedido_id          NUMBER          DEFAULT NULL,
    producto_id        NUMBER          NOT NULL,
    fecha_inicio       DATE            NOT NULL,
    fecha_fin_estimada DATE            NOT NULL,
    fecha_fin_real     DATE            DEFAULT NULL,
    estado             VARCHAR2(50)    DEFAULT 'programado' NOT NULL,
    notas              VARCHAR2(4000),
    CONSTRAINT fk_prod_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedidos(id),
    CONSTRAINT fk_prod_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE bloqueos (
    id           NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_inicio DATE            NOT NULL,
    fecha_fin    DATE            NOT NULL,
    motivo       VARCHAR2(300)   NOT NULL
);
