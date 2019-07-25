/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `device_test` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `device_id` int(10) DEFAULT NULL COMMENT '设备id',
  `last_test_date` datetime DEFAULT NULL COMMENT '上次测试时间',
  `device_nick_name` varchar(50) DEFAULT NULL,
  `test_done_date` datetime DEFAULT NULL COMMENT '测试完成时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int(1) DEFAULT NULL COMMENT '状态 0 未测试 1测试完毕',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='产线测试表';

DELETE FROM `device_test`;
/*!40000 ALTER TABLE `device_test` DISABLE KEYS */;
INSERT INTO `device_test` (`id`, `device_id`, `last_test_date`, `device_nick_name`, `test_done_date`, `remark`, `status`) VALUES
	(5, 11, NULL, '周霖的测试台灯', NULL, NULL, 0),
	(6, 5, NULL, '实验室测试版', NULL, '', 0),
	(7, 14, NULL, '小智台灯黑色办公室', NULL, NULL, 0),
	(8, 9, NULL, '黑色销售旧版', NULL, NULL, 0),
	(9, 17, NULL, '蓝色新算法6', NULL, NULL, 0),
	(10, 15, NULL, '昵称', NULL, NULL, 0),
	(11, 12, NULL, '昵称', NULL, NULL, 0),
	(12, 16, NULL, '昵称', NULL, NULL, 0),
	(13, 8, NULL, '昵称', NULL, NULL, 0),
	(14, 6, NULL, '昵称', NULL, NULL, 0),
	(15, 7, NULL, '昵称', NULL, NULL, 0),
	(16, 3, NULL, '昵称', NULL, NULL, 0),
	(17, 10, NULL, '昵称', NULL, NULL, 0),
	(18, 4, NULL, '昵称', NULL, NULL, 0),
	(19, 1, NULL, '昵称', NULL, NULL, 0),
	(20, 2, NULL, '昵称', NULL, NULL, 0),
	(25, 18, NULL, '小智台灯18', NULL, NULL, 0),
	(26, 19, NULL, '小智台灯8', NULL, NULL, 0),
	(27, 20, NULL, '小智台灯20', NULL, NULL, 0),
	(28, 21, NULL, '小智台灯21', NULL, NULL, 0),
	(29, 22, NULL, '小智台灯22', NULL, NULL, 0),
	(30, 23, '2019-01-26 12:21:37', '新版的台灯V2', NULL, NULL, 0),
	(31, 25, NULL, '小智台灯25', NULL, NULL, 0);
/*!40000 ALTER TABLE `device_test` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
