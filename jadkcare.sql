/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE `equipos` (
  `id_equipo` varchar(20) NOT NULL,
  `laboratorio` varchar(50) NOT NULL,
  `num_puesto` int NOT NULL,
  `estado_actual` varchar(20) NOT NULL, 
  `fecha_ultimo_man` date DEFAULT NULL,
  `fecha_prox_man` date DEFAULT NULL,
  PRIMARY KEY (`id_equipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `equipos` WRITE;
/*!40000 ALTER TABLE `equipos` DISABLE KEYS */;
INSERT INTO `equipos` VALUES 
('PC-LAB-05','Laboratorio A',12,'green','2026-07-10','2026-06-01'),
('PC-LAP-REAL','Laboratorio A',12,'green','2026-07-10','2026-06-01');
/*!40000 ALTER TABLE `equipos` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `especificaciones` (
  `id_equipo` varchar(20) NOT NULL,
  `cpu_modelo` varchar(50) DEFAULT NULL,
  `motherboard` varchar(50) DEFAULT NULL,
  `ram_capacidad` varchar(20) DEFAULT NULL,
  `gpu_modelo` varchar(50) DEFAULT NULL,
  `mac_address` varchar(17) DEFAULT NULL,
  `ip_address` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id_equipo`),
  CONSTRAINT `especificaciones_ibfk_1` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `especificaciones` WRITE;
/*!40000 ALTER TABLE `especificaciones` DISABLE KEYS */;
INSERT INTO `especificaciones` VALUES 
('PC-LAB-05','Intel Core i7-13700H','ASRock B760M','16 GB DDR5','NVIDIA RTX 4060','9C-7B-EF-17-BC-55','192.168.1.75'),
('PC-LAP-REAL','Intel(R) Core(TM) i5-9300H CPU @ 2.40GHz','HP Pavilion Gaming Laptop','7.84 GB','Gráficos Integrados','9C-7B-EF-17-BC-55','127.0.0.1');
/*!40000 ALTER TABLE `especificaciones` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `almacenamientos` (
  `id_almacenamiento` int NOT NULL AUTO_INCREMENT,
  `id_equipo` varchar(20) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `capacidad` varchar(20) NOT NULL,
  `uso` varchar(50) NOT NULL,
  PRIMARY KEY (`id_almacenamiento`),
  KEY `id_equipo` (`id_equipo`),
  CONSTRAINT `almacenamientos_ibfk_1` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `almacenamientos` WRITE;
/*!40000 ALTER TABLE `almacenamientos` DISABLE KEYS */;
INSERT INTO `almacenamientos` VALUES 
(1,'PC-LAB-05','NVMe','512GB','Sistema Principal'),
(2,'PC-LAB-05','SATA HDD','1TB','Respaldo'),
(3,'PC-LAP-REAL','SSD','512GB','Sistema');
/*!40000 ALTER TABLE `almacenamientos` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `alertas` (
  `id_alerta` int NOT NULL AUTO_INCREMENT,
  `id_equipo` varchar(20) NOT NULL,
  `gravedad` varchar(20) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `fecha_alerta` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado_alerta` varchar(20) DEFAULT 'Pendiente',
  PRIMARY KEY (`id_alerta`),
  KEY `id_equipo` (`id_equipo`),
  CONSTRAINT `alertas_ibfk_1` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `alertas` WRITE;
/*!40000 ALTER TABLE `alertas` DISABLE KEYS */;
INSERT INTO `alertas` VALUES 
(1,'PC-LAB-05','Alta','Mantenimiento Preventivo Expirado','2026-07-10 15:30:08','Pendiente'),
(2,'PC-LAP-REAL','Alta','Mantenimiento Preventivo Expirado','2026-07-10 16:00:55','Pendiente');
/*!40000 ALTER TABLE `alertas` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES 
(2,'Diego Hernandez','diego@jadk.com','$2a$10$X.aB8uM6B4C4x.M9qU/S5.tM8y/2oB4X3g9/aJ.h.cO4.L4uH4L/e','Administrador');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `bitacora_mantenimiento` (
  `id_mantenimiento` int NOT NULL AUTO_INCREMENT,
  `id_equipo` varchar(20) NOT NULL,
  `id_usuario` int NOT NULL,
  `tipo_mantenimiento` varchar(50) NOT NULL,
  `observaciones` text,
  `fecha_realizacion` date NOT NULL,
  PRIMARY KEY (`id_mantenimiento`),
  KEY `id_equipo` (`id_equipo`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `bitacora_mantenimiento_ibfk_1` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bitacora_mantenimiento_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;