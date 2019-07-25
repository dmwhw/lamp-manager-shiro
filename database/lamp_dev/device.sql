/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `device` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `device_uuid` varchar(32) DEFAULT NULL COMMENT '设备uuid',
  `device_nick_name` varchar(20) DEFAULT NULL COMMENT '设别别名',
  `mac` varchar(17) DEFAULT NULL COMMENT 'mac地址',
  `sn_no` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `salt` varchar(10) DEFAULT NULL,
  `device_model_id` int(5) DEFAULT NULL COMMENT '设备型号id',
  `soft_ware_version` varchar(10) DEFAULT NULL COMMENT '设备固件版本',
  `status` int(1) DEFAULT NULL COMMENT '0、待激活 1、已激活',
  `active_date` date DEFAULT NULL COMMENT '激活日期',
  `owner` int(10) DEFAULT NULL COMMENT '激活者，主人',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次在线时间',
  `ip` varchar(20) DEFAULT NULL,
  `remarkW` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_mac` (`mac`),
  KEY `uk_device_uuid` (`device_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='设备表';

DELETE FROM `device`;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
INSERT INTO `device` (`id`, `device_uuid`, `device_nick_name`, `mac`, `sn_no`, `password`, `salt`, `device_model_id`, `soft_ware_version`, `status`, `active_date`, `owner`, `last_login_time`, `ip`, `remarkW`) VALUES
	(1, 'b363abb4de40467e8551831b28d1c106', 'APP测试别用这个', 'DC:4F:22:4A:DD:84', '3VMFKRUVSPQMC5DJ', 'c7c1b030bde9f9d417819c698bb10ed8', 'a4df6075da', 1, '1.0', 1, '2018-07-24', 1, '2018-08-29 22:53:32', NULL, NULL),
	(2, 'fbf6aa7edc44478e9d290fad6a40af28', '小智台灯w', 'QWER', '3VMFMCUVSPXMC5EO', 'bb6f87c04b24556bf8cb7e1fb6cec22f', '8059321f63', 1, '1.0', 1, '2019-01-30', 1, '2019-06-17 18:22:24', '127.0.0.1', NULL),
	(3, '3d2288b9e9074b22bf2fb89c415c7443', '小白菜的台灯', 'ASDF', '3VMFIOUVSN34FSXP', 'e26dc8f5428682e9bf221ba453899498', 'cca0269927', 1, '1.0', 1, '2018-07-03', 1, '2018-09-27 18:32:00', NULL, NULL),
	(4, 'a676da88c9ad436cb492e7099e2dbcfe', 'APP测试别用这个', 'bc:dd:c2:ed:45:8c', '3VMFZ1UVSN34FT01', 'a9106cc83ed3d0e7683e863a0544c761', 'e953b0fe1b', 1, '1.0', 1, '2018-08-04', 1, '2018-08-04 11:00:06', NULL, NULL),
	(5, '233933df730941bda9399fc706183966', 'Van分愉悦♂健身房', '84:f3:eb:ba:77:e9', '3VMFHXUVSI84N423', '1703eb40109d662f5906ffc40475be19', '3cacf96f4b', 1, '1.0', 1, '2018-08-15', 1, '2018-12-29 17:15:57', NULL, NULL),
	(6, 'fbde74ffcd314188982eb49d138eb426', '样机自用', '84:f3:eb:ba:7c:ed', '3VMFUKUVSKIMJGGZ', '9b76f096b855c8f8f9ad63dfe38680c5', '67f9d69c55', 1, '1.0', 1, '2018-08-06', 3, '2018-10-07 16:08:41', NULL, NULL),
	(7, '078d9194399e41f39ed415c301ec2072', '小智台灯', '84:f3:eb:be:9e:7d', '3VMF2RUVSN34FSWA', '1f58315242802566a39c307865a02f6f', 'ba362471aa', 1, '1.0', 1, '2019-01-24', 1, '2019-01-24 11:32:32', NULL, NULL),
	(8, 'ff2b0c9d6346438281518912fc4332df', '小智台灯', '84:f3:eb:ba:7c:6e', '3VMFE8UVSI24N43H', '19e1054f040f5a351c18c943b3349e05', 'cb572bcd4d', 1, '1.0', 1, '2018-12-05', 14, '2018-12-05 23:07:09', NULL, NULL),
	(9, '035c800730124f2b91ee65dafefa9cad', '小智台灯测试旧板子', '84:f3:eb:be:98:0e', '3VMFB6UVSK6MJGHT', '681595cade121389de3bb9766c8706e0', '88bfaf3f8a', 1, '1.0', 1, '2018-11-13', 17, '2018-12-28 18:29:56', NULL, NULL),
	(10, 'ecbdf25a55404da8ab4247e0cd35f4ad', '小智台灯B', 'b4:e6:2d:4a:ca:fb', '3VMF1FUVSNM4FSZM', '469109d2272424d515d676b1a8a62381', 'f06a8eb80e', 1, '1.0', 1, '2018-09-05', 1, '2018-09-12 17:13:28', NULL, NULL),
	(11, '894ca2b9c76f4ba5b429ac34bbba22e5', '周霖的裸板台灯', 'b4:e6:2d:4a:ca:d8', '3VMFINUVSNU4FSYT', '1f8c70691d20bf20a9798d555933982b', '5036860d6e', 1, '1.0', 1, '2018-10-13', 17, '2018-12-26 19:29:14', NULL, '周霖的裸板台灯'),
	(12, '997bfdae891f4fe7b38fa887175f29d1', '小智台灯hhhh', '84:0D:8E:88:BE:31', '3VMFC6UVSIJ4N40I', 'b1534d8741cba5d52b51c24c48bb7f01', '0a62e94845', 1, '1.0', 1, '2018-10-22', 1, '2018-10-24 16:49:24', NULL, NULL),
	(14, 'dfbaed2ce7b4480693d57af6c9d00021', '新算法测试机5', 'cc:50:e3:41:1e:e3', '3VMFW7UVSPTMC5CX', '323032d9445f639908b4e84e06f7cc53', '1937589525', 1, '1.0', 1, '2018-11-19', 17, '2018-12-29 09:24:41', NULL, '新算法旧语音5'),
	(15, 'c974ae6094b642de8681fcf25b28248b', '小女儿的台灯', '80:7D:3A:4C:45:59', '3VMFFNUUPX4P2WW3', '746e59dd29b47ca9d29e63968c54e6da', '237451a6a8', 1, '1.0', 1, '2018-10-26', 3, '2018-11-06 14:08:59', NULL, NULL),
	(16, '01af1a84e5414900a650fb6425b4367a', '小智台灯3c', '84:0d:8e:88:cc:88', '3VMF0TUVSI74N41Z', 'f7c605f3fbfd18831a575106f0d6a27f', 'b5f086cf51', 1, '1.0', 1, '2018-11-02', 3, '2018-11-06 09:46:39', NULL, NULL),
	(17, 'ac99acd0519e442eb879786f9a78c1c9', '小智台灯', '84:0d:8e:88:c7:93', '3W72GGXFC904V7KT', 'f3f3e788fdca4f4b99bc3a20e9a9aa81', '50f1af5d76', 1, '1.0', 1, '2018-12-27', 63, '2018-12-27 16:13:42', NULL, '新算法旧语音6'),
	(18, 'f1841663d2ad421791dd002e30c027e8', '小智台灯', '84:0d:8e:88:cc:da', '3Z38DOG6Q2ZUHVKJ', '871c5e3f7f19d03b93d5a388ebd69775', '104d2f2b2f', 1, '9.9', 1, '2018-12-20', 60, '2019-01-05 00:00:55', NULL, '蓝色7号'),
	(19, '88550839b01b4158a14864be1d794281', '小智台灯8号', '80:7d:3a:4c:4c:0a', '3ZE1NX99CFML5VKN', '1bc62dd45bc3c5049b907e6b53c86f81', '2bbb78b351', 1, '9.9', 1, '2018-12-17', 10, '2019-01-09 12:07:12', NULL, '蓝色8号'),
	(20, '1fd2eabbe70d4c39b58d1d5177dbf579', '小智台灯', '84:0d:8e:88:cc:e4', '3ZE1SJ9GTXE4SU85', '2c05d214a10d2a712545e638c8133c47', '19a957db60', 1, '9.9', 1, '2018-12-17', 25, '2018-12-17 18:16:30', NULL, '黑色9号'),
	(21, '1cb2db2c856243dd90d1d2ae8b67e6a6', '小智台灯黑色', '84:0d:8e:88:89:20', '415LPB7RUFDI4U83', '0e6f19f8d1ead41797ef46902258b8af', '5de4c9f704', 1, '9.9', 1, '2019-01-04', 10, '2019-01-04 11:35:12', NULL, NULL),
	(22, 'b9c734185b4949f3b841367ca8a27cd5', '小智台灯黑色 灯柱', '84:0d:8e:88:c1:b0', '415NNESQGAZ5DZ4D', '85447c6ddf98ee494c5d2a8ce36cde86', 'd04e331ce8', 1, '9.9', 1, '2019-01-04', 10, '2019-01-04 12:03:53', NULL, NULL),
	(23, 'feaabd4869c046a7ad055bc58165989f', '小智台灯', 'e6:1b:58:80:9a:e6', NULL, 'a757ebf156f992608185929bae17aff9', '3a42b8adc2', 1, '2.0', 1, '2019-02-18', NULL, '2019-02-18 01:06:18', '223.73.52.226', NULL),
	(24, 'cd0cb00b09374f0cab239222d74a4d03', '小智台灯', '00:0c:29:ae:19:dd', NULL, 'd6479d519139dde4afc2e4da694a55ca', '526888ce06', 1, '999', 1, '2019-01-29', NULL, '2019-01-30 15:03:54', '113.109.248.3', NULL),
	(25, '5aecf05ce8d84b77bb08d29ede30d8be', '小智台灯', 'de:b4:fa:56:36:ff', '45BWWUM4GWOMARKK', '79a11931dcc9d10f4bbde5da7a5043f8', 'f54225f5c4', 1, '2.0', 1, '2019-02-15', 1, '2019-03-28 16:24:06', '113.109.248.197', NULL);
/*!40000 ALTER TABLE `device` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
