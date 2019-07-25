/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `device_import_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `backend_id` int(5) DEFAULT NULL,
  `log` varchar(255) DEFAULT NULL,
  `status` varchar(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=utf8;

DELETE FROM `device_import_log`;
/*!40000 ALTER TABLE `device_import_log` DISABLE KEYS */;
INSERT INTO `device_import_log` (`id`, `file_name`, `backend_id`, `log`, `status`, `time`) VALUES
	(285, '台灯mac导入模板 (7).xlsx_67d51dda-dd5b-4a06-88b4-529956dd2357.xlsx', 1, '相同数据:0,数据库重复数据:0,错误MAC:1,错误型号代码:0', '检查', '2018-11-19 10:12:01'),
	(286, '台灯mac导入模板 (7).xlsx_9210f520-705d-4866-a49e-88a96b1854f4.xlsx', 1, '相同数据:0,数据库重复数据:0,错误MAC:0,错误型号代码:0', '检查', '2018-11-19 10:12:22'),
	(287, '台灯mac导入模板 (7).xlsx_783554d1-ec37-43c8-883e-e46950c9728e.xlsx', 1, '相同数据:0,数据库重复数据:0,错误MAC:0,错误型号代码:0', '检查', '2018-11-19 10:12:24'),
	(288, '台灯mac导入模板 (7).xlsx_6680dfb9-bd9e-4821-bbb3-4931663b86a9.xlsx', 1, '相同数据:0,数据库重复数据:0,错误MAC:0,错误型号代码:0', '检查', '2018-11-19 10:12:26'),
	(289, '台灯mac导入模板 (7).xlsx_75ab1817-a9f9-4929-904e-e9aaae31a207.xlsx', 1, '相同数据:0,数据库重复数据:0,错误MAC:0,错误型号代码:0', '检查', '2018-11-19 10:12:27'),
	(290, '台灯mac导入模板 (7).xlsx_c7335506-524e-4dd4-9ad1-eeb0830039a1.xlsx', 1, '相同数据:0,数据库重复数据:0,错误MAC:0,错误型号代码:0', '检查', '2018-11-19 10:12:28'),
	(291, '/usr/local/tomcat/lamp-manage/excel/log_2018-12-14_15_27_47.log', NULL, '通过陌生Mac页导入', '陌生Mac导入', '2018-12-14 15:27:47'),
	(292, '/usr/local/tomcat/lamp-manage/excel/log_2018-12-17_16_06_20.log', NULL, '通过陌生Mac页导入', '陌生Mac导入', '2018-12-17 16:06:21'),
	(293, '/usr/local/tomcat/lamp-manage/excel/log_2018-12-17_16_06_24.log', NULL, '通过陌生Mac页导入', '陌生Mac导入', '2018-12-17 16:06:25'),
	(294, '/usr/local/tomcat/lamp-manage/excel/log_2019-01-04_11_34_33.log', NULL, '通过陌生Mac页导入', '陌生Mac导入', '2019-01-04 11:34:33'),
	(295, '/usr/local/tomcat/lamp-manage/excel/log_2019-01-04_12_03_30.log', NULL, '通过陌生Mac页导入', '陌生Mac导入', '2019-01-04 12:03:30'),
	(296, '/usr/local/tomcat/lamp-manage/excel/log_2019-02-15_14_36_47.log', NULL, '通过陌生Mac页导入', '陌生Mac导入', '2019-02-15 14:36:47');
/*!40000 ALTER TABLE `device_import_log` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
