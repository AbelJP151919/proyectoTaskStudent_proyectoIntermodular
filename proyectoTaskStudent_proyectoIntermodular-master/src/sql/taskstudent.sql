-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: taskstudent
-- ------------------------------------------------------
-- Server version	8.0.44

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

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK6nh9hir3e65odwps0rhdvja0g` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (1);
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alumnos`
--

DROP TABLE IF EXISTS `alumnos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alumnos` (
  `id` bigint NOT NULL,
  `curso` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK4kb1nf96cr2g4p6b5k2wxmvu2` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumnos`
--

LOCK TABLES `alumnos` WRITE;
/*!40000 ALTER TABLE `alumnos` DISABLE KEYS */;
INSERT INTO `alumnos` VALUES (5,'3ºPrimaria'),(13,'1ºBachillerato');
/*!40000 ALTER TABLE `alumnos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alumnos_asignaturas`
--

DROP TABLE IF EXISTS `alumnos_asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alumnos_asignaturas` (
  `alumno_id` bigint NOT NULL,
  `asignatura_id` bigint NOT NULL,
  KEY `FKlfrtfik2cgyv7f5ka7ba7ggoc` (`asignatura_id`),
  KEY `FKn75nepq3anqe4gshn91emarb1` (`alumno_id`),
  CONSTRAINT `FKlfrtfik2cgyv7f5ka7ba7ggoc` FOREIGN KEY (`asignatura_id`) REFERENCES `asignaturas` (`id`),
  CONSTRAINT `FKn75nepq3anqe4gshn91emarb1` FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumnos_asignaturas`
--

LOCK TABLES `alumnos_asignaturas` WRITE;
/*!40000 ALTER TABLE `alumnos_asignaturas` DISABLE KEYS */;
INSERT INTO `alumnos_asignaturas` VALUES (13,4),(13,6);
/*!40000 ALTER TABLE `alumnos_asignaturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asignaturas`
--

DROP TABLE IF EXISTS `asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `aula` varchar(255) NOT NULL,
  `codigo` varchar(255) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `horario` varchar(255) DEFAULT NULL,
  `horas_totales` int NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `profesor_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1qw6mq2y799h57v92l4g9pn3c` (`codigo`),
  KEY `FK9u8wu1fkyvdpwr3pmhd5opth7` (`profesor_id`),
  CONSTRAINT `FK9u8wu1fkyvdpwr3pmhd5opth7` FOREIGN KEY (`profesor_id`) REFERENCES `profesores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignaturas`
--

LOCK TABLES `asignaturas` WRITE;
/*!40000 ALTER TABLE `asignaturas` DISABLE KEYS */;
INSERT INTO `asignaturas` VALUES (1,'A-100','MAT-100','Asignatura de Matematicas 1ºBachillerato','2025-12-11','2023-05-12','Lun-Miercoles',210,'Matematicas ',2),(3,'232','AAA','343434134','2026-01-30','2026-01-16','Lun-Miercoles',222,'Abel',2),(4,'232','AAA111','asdsadasdasdasdasd','2026-01-29','2026-01-14','Lun-Miercoles',222,'Fisica',2),(5,'','','','2026-01-29','2026-01-23','23',123,'',2),(6,'A-100','WAT111','asdasdasdasdasdasd','2026-02-12','2026-01-15','Lun-Miercoles',222,'War Thunder',14);
/*!40000 ALTER TABLE `asignaturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asignaturas_alumnos`
--

DROP TABLE IF EXISTS `asignaturas_alumnos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaturas_alumnos` (
  `asignaturas_id` bigint NOT NULL,
  `alumnos_id` bigint NOT NULL,
  KEY `FKiebptn1nn9o44m89gsj0rko2j` (`alumnos_id`),
  KEY `FKrw6mqh46yx4s9trewfrb1fivv` (`asignaturas_id`),
  CONSTRAINT `FKiebptn1nn9o44m89gsj0rko2j` FOREIGN KEY (`alumnos_id`) REFERENCES `alumnos` (`id`),
  CONSTRAINT `FKrw6mqh46yx4s9trewfrb1fivv` FOREIGN KEY (`asignaturas_id`) REFERENCES `asignaturas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignaturas_alumnos`
--

LOCK TABLES `asignaturas_alumnos` WRITE;
/*!40000 ALTER TABLE `asignaturas_alumnos` DISABLE KEYS */;
/*!40000 ALTER TABLE `asignaturas_alumnos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenido`
--

DROP TABLE IF EXISTS `contenido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenido` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(1000) DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `titulo` varchar(255) NOT NULL,
  `tema_id` bigint DEFAULT NULL,
  `tipo` tinyint DEFAULT NULL,
  `nombre_archivo` varchar(255) DEFAULT NULL,
  `ruta_archivo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsbiq9cs6j0eokv4wi89gwwr5k` (`tema_id`),
  CONSTRAINT `FKsbiq9cs6j0eokv4wi89gwwr5k` FOREIGN KEY (`tema_id`) REFERENCES `temas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenido`
--

LOCK TABLES `contenido` WRITE;
/*!40000 ALTER TABLE `contenido` DISABLE KEYS */;
INSERT INTO `contenido` VALUES (5,'asdasdasdasdasd',NULL,'Recurso',1,3,'Propuesta de Proyecto.pdf','uploads\\contenidos\\Propuesta de Proyecto.pdf'),(7,'Matrices Afines',NULL,'Matrices Afines',1,0,NULL,NULL),(10,'sdfsdfsdfsdfsdf','2026-01-30','dsfsfdsdfsdf',7,0,NULL,NULL),(12,'asdasdasdasdasdasdasdasd','2026-01-30','MAMMSAM',7,0,NULL,NULL),(13,'asdadasdasdasdsad','2026-01-26','Russian Bias',9,0,NULL,NULL);
/*!40000 ALTER TABLE `contenido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entregas`
--

DROP TABLE IF EXISTS `entregas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entregas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `archivo_nombre` varchar(255) DEFAULT NULL,
  `archivo_ruta` varchar(255) DEFAULT NULL,
  `comentario` text,
  `fecha_entrega` datetime(6) DEFAULT NULL,
  `nota` double DEFAULT NULL,
  `revisada` bit(1) DEFAULT NULL,
  `alumno_id` bigint DEFAULT NULL,
  `tarea_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8s59nl0ts7t855d87ysx4w8bv` (`alumno_id`),
  KEY `FKphnnt472ctfq79m766ulhrbl2` (`tarea_id`),
  CONSTRAINT `FK8s59nl0ts7t855d87ysx4w8bv` FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`),
  CONSTRAINT `FKphnnt472ctfq79m766ulhrbl2` FOREIGN KEY (`tarea_id`) REFERENCES `contenido` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entregas`
--

LOCK TABLES `entregas` WRITE;
/*!40000 ALTER TABLE `entregas` DISABLE KEYS */;
INSERT INTO `entregas` VALUES (3,'Propuesta de Proyecto (1).pdf','uploads\\entregas\\Propuesta de Proyecto (1).pdf','','2026-01-25 21:18:37.669305',NULL,_binary '\0',13,10),(4,'Propuesta de Proyecto (2).pdf','uploads\\entregas\\Propuesta de Proyecto (2).pdf','','2026-01-25 21:19:04.201236',NULL,_binary '\0',13,12),(5,'CV.pdf','uploads\\entregas\\CV.pdf','Hola\n\n[Profesor]: Eliminen a rusia del WT','2026-01-25 21:51:26.109436',10,_binary '',13,13);
/*!40000 ALTER TABLE `entregas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materiales`
--

DROP TABLE IF EXISTS `materiales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materiales` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `asignatura_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfnmd2nbn1xujdta3yqjuowsff` (`asignatura_id`),
  CONSTRAINT `FKfnmd2nbn1xujdta3yqjuowsff` FOREIGN KEY (`asignatura_id`) REFERENCES `asignaturas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales`
--

LOCK TABLES `materiales` WRITE;
/*!40000 ALTER TABLE `materiales` DISABLE KEYS */;
/*!40000 ALTER TABLE `materiales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesores`
--

DROP TABLE IF EXISTS `profesores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profesores` (
  `id` bigint NOT NULL,
  `departamento` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKgm6359ytbli6acd1fvwf3gny2` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesores`
--

LOCK TABLES `profesores` WRITE;
/*!40000 ALTER TABLE `profesores` DISABLE KEYS */;
INSERT INTO `profesores` VALUES (2,'Lengua'),(14,'Matematicas');
/*!40000 ALTER TABLE `profesores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tareas`
--

DROP TABLE IF EXISTS `tareas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tareas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `titulo` varchar(255) NOT NULL,
  `asignatura_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKscx6huc2pt0yjxmotyvjhv3lv` (`asignatura_id`),
  CONSTRAINT `FKscx6huc2pt0yjxmotyvjhv3lv` FOREIGN KEY (`asignatura_id`) REFERENCES `asignaturas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tareas`
--

LOCK TABLES `tareas` WRITE;
/*!40000 ALTER TABLE `tareas` DISABLE KEYS */;
/*!40000 ALTER TABLE `tareas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temas`
--

DROP TABLE IF EXISTS `temas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `temas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(1000) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `orden` int NOT NULL,
  `asignatura_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1xfb08ekrvwyrw6f5qqwts7cy` (`asignatura_id`),
  CONSTRAINT `FK1xfb08ekrvwyrw6f5qqwts7cy` FOREIGN KEY (`asignatura_id`) REFERENCES `asignaturas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temas`
--

LOCK TABLES `temas` WRITE;
/*!40000 ALTER TABLE `temas` DISABLE KEYS */;
INSERT INTO `temas` VALUES (1,'Herencias de matematicas','Hola',1,1),(2,'sdasdadsdsafsadf','Abel',1,1),(7,'dfasdfd','Pedro',1,4),(8,'dasdasdasdasd','Abel',1,1),(9,'sdadadasdasd','Josefa',1,6);
/*!40000 ALTER TABLE `temas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conn` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `nombre_usuario` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`),
  UNIQUE KEY `UKof5vabgukahdwmgxk4kjrbu98` (`nombre_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'$2a$10$rdhPn5JpD0FuKxoGKyPtb.tvd7U/AMrKqtzX4WPgiqHWLsxJpmbh.','administrador@gmail.com','Abel Jiménez','administrador'),(2,'12345678','fdDFADFASFD@gmail.com','Francisco Jose','Jesus'),(5,'12345678','miduarter@alumnos.unex.es','Miguel','Nicolas Maduro'),(13,'$2a$10$YP4FgHEIzcNWWNY42OH41u.Lzwz/YdMM01/kPA6bZIizEvd/6Qmo6','aaaaaa@gmail.com','Abel','Abelin'),(14,'$2a$10$nP3RBDvp9BOBm6KCSRiHweaEjrJMUaNIvdtjYtQqbvX/Hwx5YVIr6','qweqweq@gmail.com','Pedro','Pedrito');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-26 19:28:52
