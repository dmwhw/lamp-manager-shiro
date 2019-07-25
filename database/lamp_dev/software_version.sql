/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `software_version` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `model_no_id` int(5) NOT NULL COMMENT '设备型号id',
  `main_version` int(3) NOT NULL COMMENT '主版本',
  `sub_version` int(3) NOT NULL COMMENT '子版本',
  `version_name` varchar(20) DEFAULT NULL COMMENT '版本名称',
  `android_min_version` varchar(10) DEFAULT NULL,
  `ios_min_version` varchar(10) DEFAULT NULL,
  `next_version_id` int(5) DEFAULT NULL COMMENT '可更新的版本',
  `type` int(1) DEFAULT NULL COMMENT '升级选项0无需更新、1可选更新、2强制更新、4公测版本',
  `post_time` datetime DEFAULT NULL COMMENT '提交日期',
  `publish_date` datetime DEFAULT NULL COMMENT '发布日期',
  `status` int(1) DEFAULT NULL COMMENT '状态0、提交 1、已发布',
  `remark` varchar(255) DEFAULT NULL COMMENT '版本说明',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_model_version` (`model_no_id`,`main_version`,`sub_version`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='设备固件版本登记';

DELETE FROM `software_version`;
/*!40000 ALTER TABLE `software_version` DISABLE KEYS */;
INSERT INTO `software_version` (`id`, `model_no_id`, `main_version`, `sub_version`, `version_name`, `android_min_version`, `ios_min_version`, `next_version_id`, `type`, `post_time`, `publish_date`, `status`, `remark`) VALUES
	(1, 1, 1, 0, '1.0', '1.1', '1.1.0', 4, 1, '2018-08-18 16:17:05', '2018-08-18 16:17:07', 1, NULL),
	(2, 1, 1, 1, '1.1', '1.1', '1.1.0', 0, 0, '2018-08-18 16:17:05', '2018-08-18 16:17:07', 1, '這次版本做了一個很大的改動\r\n我們優化了坐姿算法以及圖像傳輸的速度。\r\n'),
	(3, 1, 9, 9, '9.9', '1.1', '1.1.0', 4, 1, '2018-10-13 14:44:04', '2018-10-13 14:44:08', 1, '内测版本。这是内测的。'),
	(4, 1, 10, 0, '10.0', '1.1', '1.1.0', NULL, 0, '2018-10-13 14:44:58', '2018-10-13 14:44:59', 1, '内测！发布了100次！');
/*!40000 ALTER TABLE `software_version` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
