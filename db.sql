-- MySQL dump 10.13  Distrib 9.6.0, for macos26.2 (arm64)
--
-- Host: localhost    Database: jogger_db
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activities` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `calories` int NOT NULL,
  `date` date NOT NULL,
  `distance_km` double NOT NULL,
  `duration_sec` int NOT NULL,
  `route` varchar(255) DEFAULT NULL,
  `runner_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo8fgr8n0m5tlfgpqf0ih452w3` (`runner_id`),
  CONSTRAINT `FKo8fgr8n0m5tlfgpqf0ih452w3` FOREIGN KEY (`runner_id`) REFERENCES `runners` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `badges`
--

DROP TABLE IF EXISTS `badges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `badges` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `awarded_at` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `runner_id` bigint DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `seen` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2lnld9q6xcpf2ngghixfjduw0` (`runner_id`,`code`),
  CONSTRAINT `FKpyshpukvstphxwj5aqdirsq45` FOREIGN KEY (`runner_id`) REFERENCES `runners` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `runners`
--

DROP TABLE IF EXISTS `runners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `runners` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_joined` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `weight` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhj9kyi9m63so71vqd1eops2xi` (`email`),
  UNIQUE KEY `UK71tg5v9di7vwh1e642kiin8nr` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_follows`
--

DROP TABLE IF EXISTS `user_follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_follows` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `followed_id` bigint NOT NULL,
  `follower_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user_follows` (`follower_id`,`followed_id`),
  KEY `FK_user_follows_followed` (`followed_id`),
  KEY `FK_user_follows_follower` (`follower_id`),
  CONSTRAINT `FK_user_follows_followed` FOREIGN KEY (`followed_id`) REFERENCES `runners` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_user_follows_follower` FOREIGN KEY (`follower_id`) REFERENCES `runners` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `runners`
--

LOCK TABLES `runners` WRITE;
/*!40000 ALTER TABLE `runners` DISABLE KEYS */;
INSERT INTO `runners` (`id`, `username`, `email`, `password_hash`, `role`, `weight`, `date_joined`) VALUES
(1, 'admin', 'admin@jogger.com', '$2a$10$gtNvuRxihC0zd6yfCkDr1.hgHQ7nZYVtJLsxFBgA5WuyL9n2d3gZC', 'ROLE_ADMIN', 75.0, '2026-01-01 10:00:00'),
(2, 'alex_marathon', 'alex@jogger.com', '$2a$10$gtNvuRxihC0zd6yfCkDr1.hgHQ7nZYVtJLsxFBgA5WuyL9n2d3gZC', 'ROLE_USER', 68.0, '2026-02-10 09:30:00'),
(3, 'sarah_runs', 'sarah@jogger.com', '$2a$10$gtNvuRxihC0zd6yfCkDr1.hgHQ7nZYVtJLsxFBgA5WuyL9n2d3gZC', 'ROLE_USER', 58.0, '2026-02-15 11:15:00'),
(4, 'david_pace', 'david@jogger.com', '$2a$10$gtNvuRxihC0zd6yfCkDr1.hgHQ7nZYVtJLsxFBgA5WuyL9n2d3gZC', 'ROLE_USER', 74.0, '2026-03-01 14:00:00'),
(5, 'demo_runner', 'demo@jogger.com', '$2a$10$gtNvuRxihC0zd6yfCkDr1.hgHQ7nZYVtJLsxFBgA5WuyL9n2d3gZC', 'ROLE_USER', 70.0, '2026-03-15 16:20:00');
/*!40000 ALTER TABLE `runners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` (`id`, `calories`, `date`, `distance_km`, `duration_sec`, `route`, `runner_id`) VALUES
(1, 1480, '2026-03-10', 21.1, 6300, 'Riverside Half-Marathon', 2),
(2, 1050, '2026-03-15', 15.0, 4500, 'Hill Trail Challenge', 2),
(3, 1260, '2026-03-20', 18.0, 5400, 'Long Weekend Run', 2),
(4, 1120, '2026-03-24', 16.0, 4800, 'Tempo Park Loop', 2),
(5, 1040, '2026-03-28', 14.9, 4400, 'Evening City Loop', 2),
(6, 720, '2026-03-12', 12.0, 3960, 'Lake Shore Trail', 3),
(7, 600, '2026-03-16', 10.0, 3300, 'Sunset 10K', 3),
(8, 900, '2026-03-22', 15.0, 4950, 'Forest Nature Path', 3),
(9, 480, '2026-03-25', 8.0, 2640, 'Morning Recovery Jog', 3),
(10, 450, '2026-03-27', 7.5, 2475, 'Quick Park Interval', 3),
(11, 840, '2026-03-14', 11.0, 3630, 'Downtown Sunrise Run', 4),
(12, 770, '2026-03-18', 10.0, 3300, 'Neighborhood Circuit', 4),
(13, 770, '2026-03-26', 10.0, 3300, 'Campus 10K', 4);
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_follows`
--

LOCK TABLES `user_follows` WRITE;
/*!40000 ALTER TABLE `user_follows` DISABLE KEYS */;
INSERT INTO `user_follows` (`id`, `follower_id`, `followed_id`, `created_at`) VALUES
(1, 5, 2, '2026-03-16 10:00:00'),
(2, 5, 3, '2026-03-16 10:05:00'),
(3, 4, 2, '2026-03-17 12:00:00');
/*!40000 ALTER TABLE `user_follows` ENABLE KEYS */;
UNLOCK TABLES;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-29 16:05:28
