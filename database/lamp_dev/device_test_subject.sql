/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `device_test_subject` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `device_id` int(10) DEFAULT NULL COMMENT '设备id',
  `subject_code` varchar(255) DEFAULT NULL COMMENT '项目代码',
  `last_test_time` datetime DEFAULT NULL COMMENT '上次测试时间',
  `status` int(1) DEFAULT NULL COMMENT '状态 0 未测试 1测试完毕',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='产线测试项目表';

DELETE FROM `device_test_subject`;
/*!40000 ALTER TABLE `device_test_subject` DISABLE KEYS */;
INSERT INTO `device_test_subject` (`id`, `device_id`, `subject_code`, `last_test_time`, `status`, `remark`) VALUES
	(1, 11, 'camera_test', '2018-11-20 17:47:11', 1, NULL),
	(2, 11, 'ligth_level_test', '2018-11-20 17:48:54', 1, NULL),
	(3, 11, 'light_mode_test', '2018-11-20 17:48:55', 1, NULL),
	(4, 9, 'voice_test', '2018-11-20 17:50:47', 1, NULL),
	(5, 9, 'volume_test', '2018-11-20 17:51:31', 1, NULL),
	(6, 5, 'voice_test', '2018-11-24 16:43:18', 1, '掉线'),
	(7, 5, 'ligth_level_test', '2018-11-21 15:21:42', 1, NULL),
	(8, 14, 'camera_test', '2018-11-21 16:30:37', 1, NULL),
	(9, 5, 'camera_test', '2018-12-27 22:19:45', 1, NULL),
	(10, 23, 'volume_test', '2019-02-07 10:14:53', 1, NULL);
/*!40000 ALTER TABLE `device_test_subject` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
