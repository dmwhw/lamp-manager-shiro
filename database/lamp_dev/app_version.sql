/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `app_version` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `version_name` varchar(10) DEFAULT NULL COMMENT '版本号',
  `next_version_name` varchar(10) DEFAULT NULL COMMENT '可更新的版本号',
  `system` int(1) DEFAULT NULL COMMENT '系统1,安卓 2苹果',
  `status` int(1) DEFAULT NULL COMMENT '状态0,无需更新 1,,可更新',
  `date` date DEFAULT NULL COMMENT '发布日期',
  `remark` varchar(200) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_version_name_system` (`version_name`,`system`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='App版本升级表';

DELETE FROM `app_version`;
/*!40000 ALTER TABLE `app_version` DISABLE KEYS */;
INSERT INTO `app_version` (`id`, `version_name`, `next_version_name`, `system`, `status`, `date`, `remark`) VALUES
	(3, '1.3', '2.0', 1, 1, '2019-01-11', NULL),
	(4, '1.3.0', '2.0.0', 2, 1, '2019-01-11', NULL),
	(5, '2.0.0', NULL, 2, 0, '2019-01-11', '最新版本6.6.6！\r\n\r\n1、呃呃呃2、杀了一只鸡，祭天，台灯大卖\r\n3、天啊\r\n4、APP做了优化，变得无所不能！！！\r\n'),
	(6, '2.0', NULL, 1, 0, '2019-01-11', '最新版本6.6.6\r\n\r\n1、啊啊啊\r\n2、杀了一只鸡，祭天，台灯大卖\r\n3、天啊\r\n4、APP做了优化，变得无所不能！！！\r\n');
/*!40000 ALTER TABLE `app_version` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
