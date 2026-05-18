-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: db_categories
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `db_categories`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db_categories` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `db_categories`;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categorias` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` int(11) NOT NULL,
  `descripcion` varchar(300) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto_categoria`
--

DROP TABLE IF EXISTS `producto_categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_categoria` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) NOT NULL,
  `categoria_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKck76h1dqwbw3rp8gkxkxytqe6` (`categoria_id`),
  CONSTRAINT `FKck76h1dqwbw3rp8gkxkxytqe6` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_categoria`
--

LOCK TABLES `producto_categoria` WRITE;
/*!40000 ALTER TABLE `producto_categoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto_categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `db_materials`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db_materials` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `db_materials`;

--
-- Table structure for table `materiales`
--

DROP TABLE IF EXISTS `materiales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `materiales` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(4000) DEFAULT NULL,
  `fecha_actualizacion` date DEFAULT NULL,
  `nombre` varchar(150) NOT NULL,
  `precio_unitario` double NOT NULL,
  `stock_actual` double NOT NULL,
  `stock_minimo` double NOT NULL,
  `unidad_medida` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales`
--

LOCK TABLES `materiales` WRITE;
/*!40000 ALTER TABLE `materiales` DISABLE KEYS */;
INSERT INTO `materiales` VALUES (1,'Hilo encerado para pulseras y collares','2026-05-17','Hilo encerado',150,200,20,'metros'),(2,'Mostacilla de vidrio pequeña para tejidos','2026-05-17','Mostacilla',80,500,50,'gramos'),(3,'Perla redonda de acrílico blanca','2026-05-17','Perla de acrílico',20,300,30,'unidades'),(4,'Cierre metálico dorado para collares y pulseras','2026-05-17','Cierre de langosta',50,100,15,'unidades'),(5,'Cadena fina de acero inoxidable plateada','2026-05-17','Cadena de acero',900,50,10,'metros'),(6,'Cristal facetado transparente para aretes y collares','2026-05-17','Cristal facetado',120,150,25,'unidades'),(7,'Alambre fino de cobre para envoltura de piedras','2026-05-17','Alambre de cobre',300,80,10,'metros');
/*!40000 ALTER TABLE `materiales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `db_orders`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db_orders` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `db_orders`;

--
-- Table structure for table `pedido_material`
--

DROP TABLE IF EXISTS `pedido_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido_material` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad_reservada` decimal(38,2) NOT NULL,
  `material_id` bigint(20) NOT NULL,
  `pedido_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf0itg50ptgllkj3e3km2ygb5f` (`pedido_id`),
  CONSTRAINT `FKf0itg50ptgllkj3e3km2ygb5f` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_material`
--

LOCK TABLES `pedido_material` WRITE;
/*!40000 ALTER TABLE `pedido_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedidos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `abono_pagado` decimal(38,2) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `descripcion` varchar(4000) DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `fecha_pedido` date DEFAULT NULL,
  `total` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (1,0.00,1,'Pulsera personalizada con nombre','en_proceso','2026-06-01','2026-05-13',5000.00);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `db_products`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db_products` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `db_products`;

--
-- Table structure for table `producto_material`
--

DROP TABLE IF EXISTS `producto_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_material` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad_requerida` double NOT NULL,
  `material_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2q3oivgngly6905gfkt7u8n5a` (`producto_id`),
  CONSTRAINT `FK2q3oivgngly6905gfkt7u8n5a` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_material`
--

LOCK TABLES `producto_material` WRITE;
/*!40000 ALTER TABLE `producto_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `descripcion` varchar(4000) DEFAULT NULL,
  `nombre` varchar(150) NOT NULL,
  `precio_venta` double NOT NULL,
  `stock_disponible` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'','Aretes colgantes con cristal facetado y argolla dorada','Aretes de cristal',4000,20),(2,'','Collar con perlas de acrílico y cierre de langosta','Collar de perlas',5000,10),(3,'','Aretes colgantes con cristal facetado y argolla dorada','Aretes de cristal',4000,20),(4,'','Pulsera trenzada con hilo encerado de colores','Pulsera de hilo encerado',2500,25),(5,'','Colgante con piedra natural de cuarzo rosa envuelta en alambre de cobre','Colgante de cuarzo',7000,8),(6,'','Collar minimalista de cadena de acero con cierre de langosta','Collar de cadena fina',6000,12);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `db_ventas`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db_ventas` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `db_ventas`;

--
-- Table structure for table `venta_producto`
--

DROP TABLE IF EXISTS `venta_producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `venta_producto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` double NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `venta_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqut6au6m094vc90c0o73tqjrn` (`venta_id`),
  CONSTRAINT `FKqut6au6m094vc90c0o73tqjrn` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta_producto`
--

LOCK TABLES `venta_producto` WRITE;
/*!40000 ALTER TABLE `venta_producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `venta_producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ventas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint(20) NOT NULL,
  `comprobante_ref` varchar(100) DEFAULT NULL,
  `fecha` date NOT NULL,
  `metodo_pago` varchar(50) NOT NULL,
  `total` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-17 21:50:50
