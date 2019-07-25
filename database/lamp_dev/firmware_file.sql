/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `firmware_file` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `version_id` int(5) DEFAULT NULL COMMENT '版本id',
  `firmware_file_index` int(3) DEFAULT NULL COMMENT '文件序号',
  `firmware_file_type` int(3) DEFAULT NULL COMMENT '固件类型EH',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件地址:请使用绝对路径',
  `check_sum` int(3) DEFAULT NULL COMMENT '文件checksum',
  `file_md5` varchar(32) DEFAULT NULL,
  `file_length` int(10) DEFAULT NULL COMMENT '文件长度(字节)',
  `flash_addr` int(5) DEFAULT NULL COMMENT '烧录位置',
  `upload_Date` datetime DEFAULT NULL COMMENT '上传日期',
  `user_id` int(10) DEFAULT NULL COMMENT '上传人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注,自己看的',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_version_filetype` (`version_id`,`firmware_file_type`,`firmware_file_index`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='固件文件列表';

DELETE FROM `firmware_file`;
/*!40000 ALTER TABLE `firmware_file` DISABLE KEYS */;
INSERT INTO `firmware_file` (`id`, `version_id`, `firmware_file_index`, `firmware_file_type`, `name`, `file_path`, `check_sum`, `file_md5`, `file_length`, `flash_addr`, `upload_Date`, `user_id`, `remark`) VALUES
	(1, 2, 1, 1, 'App', '/usr/local/tomcat/updateFiles/2-1.1/2018_10_17_133102_oApp', 123, '513D8A6BD3EF012087DBA3F8D5D4124B', 104924, 0, '2018-10-17 13:31:02', 1, ''),
	(2, 2, 16, 16, 'blank.bin', '/usr/local/tomcat/updateFiles/2-1.1/2018_10_07_185354_blank1.bin', 0, '6AE59E64850377EE5470C854761551EA', 4096, 516096, '2018-10-07 18:53:54', 1, ''),
	(3, 2, 32, 16, 'text.bin', '/usr/local/tomcat/updateFiles/2-1.1/2018_10_17_133102_eagle.irom0text.bin', 125, 'EDA9E207B10B8D4405EBFBFAAE91746C', 340000, 262144, '2018-10-17 13:31:02', 1, ''),
	(4, 2, 64, 16, 'flash.bin', '/usr/local/tomcat/updateFiles/2-1.1/2018_10_17_133102_eagle.flash.bin', 122, 'F1003D179C0136108F5AFBD898218D99', 40304, 0, '2018-10-17 13:31:02', 1, ''),
	(5, 2, 128, 16, 'rf.bin', '/usr/local/tomcat/updateFiles/2-1.1/2018_10_07_185354_rf1.bin', 236, '95659619C2CD734463C6604736A564F8', 128, 507904, '2018-10-07 18:53:54', 1, ''),
	(10, 4, 1, 1, 'App', '/usr/local/tomcat/updateFiles/4-10.0/2018_12_13_005146_App', 24, '66740B17B9ACA4947EB9467EFB010C34', 144360, 0, '2018-12-13 00:51:47', 1, NULL),
	(11, 4, 16, 16, 'blank.bin', '/usr/local/tomcat/updateFiles/4-10.0/2018_10_18_175624_blank1.bin', 0, '6AE59E64850377EE5470C854761551EA', 4096, 516096, '2018-10-18 17:56:24', 1, ''),
	(12, 4, 32, 16, 'text.bin', '/usr/local/tomcat/updateFiles/4-10.0/2019_01_04_121814_eagle.irom0text.bin', 83, 'F9AB850508A6AB42D0085B845377565F', 343120, 262144, '2019-01-04 12:18:15', 1, ''),
	(13, 4, 64, 16, 'flash.bin', '/usr/local/tomcat/updateFiles/4-10.0/2019_01_04_121814_eagle.flash.bin', 103, '7CD57FAF831D64BD77F94E1F320AF0B9', 40448, 0, '2019-01-04 12:18:15', 1, ''),
	(14, 4, 128, 16, 'rf.bin', '/usr/local/tomcat/updateFiles/4-10.0/2018_10_18_175624_rf1.bin', 236, '95659619C2CD734463C6604736A564F8', 128, 507904, '2018-10-18 17:56:24', 1, '');
/*!40000 ALTER TABLE `firmware_file` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
