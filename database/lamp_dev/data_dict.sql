/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `data_dict` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `module` varchar(20) NOT NULL COMMENT '模块',
  `type_name` varchar(64) DEFAULT NULL COMMENT '字典中文名',
  `type_code` varchar(64) NOT NULL COMMENT '字典代码名',
  `item_name` varchar(64) DEFAULT NULL COMMENT '值名',
  `item_code` varchar(64) NOT NULL COMMENT '值代码名',
  `item_value` varchar(128) DEFAULT NULL COMMENT '数值',
  `sort` int(2) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '字典备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_key_value` (`module`,`type_code`,`item_code`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8 COMMENT='字典表';

DELETE FROM `data_dict`;
/*!40000 ALTER TABLE `data_dict` DISABLE KEYS */;
INSERT INTO `data_dict` (`id`, `module`, `type_name`, `type_code`, `item_name`, `item_code`, `item_value`, `sort`, `remark`) VALUES
	(1, 'common', '性别', 'sex', '男性', 'male', 'M', 1, NULL),
	(2, 'common', '性别', 'sex', '女性', 'female', 'F', 0, NULL),
	(3, 'common', '在线状态', 'isOnline', '离线', 'offline', '0', 0, NULL),
	(4, 'common', '在线状态', 'isOnline', '在线', 'online', '1', 1, NULL),
	(5, 'common', '闹钟铃声类型', 'alarmVoiceType', '内置', 'preinstall', '1', 0, '台灯内置的铃声'),
	(6, 'common', '闹钟铃声类型', 'alarmVoiceType', '铃声', 'upload', '2', 1, '自定义上传的铃声'),
	(7, 'common', '闹钟铃声类型', 'alarmVoiceType', '录音', 'record', '3', 2, '用户自定义录音的内容'),
	(8, 'common', '闹钟响铃次数', 'alarmRepeat', '无限响铃', 'unlimited', '0', 0, NULL),
	(9, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃1次', 'repeat1', '1', 1, NULL),
	(10, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃2次', 'repeat2', '2', 2, NULL),
	(11, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃3次', 'repeat3', '3', 3, NULL),
	(12, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃4次', 'repeat4', '4', 4, NULL),
	(13, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃5次', 'repeat5', '5', 5, NULL),
	(15, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃6次', 'repeat6', '6', 6, NULL),
	(16, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃7次', 'repeat7', '7', 7, NULL),
	(17, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃8次', 'repeat8', '8', 8, NULL),
	(18, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃9次', 'repeat9', '9', 9, NULL),
	(19, 'common', '闹钟响铃次数', 'alarmRepeat', '响铃10次', 'repeat10', '10', 10, NULL),
	(20, 'common', '闹钟灯光操作', 'alarmIsTurnLightOn', '不做任何操作', 'noAction', '-1', 0, NULL),
	(21, 'common', '闹钟响铃次数范围', 'alarmRepeatRange', '最小值', 'min', '0', 0, NULL),
	(22, 'common', '闹钟响铃次数范围', 'alarmRepeatRange', '最大值', 'max', '10', 1, NULL),
	(23, 'common', '闹钟灯光操作', 'alarmIsTurnLightOn', '开灯', 'on', '1', 0, NULL),
	(24, 'common', '闹钟灯光操作', 'alarmIsTurnLightOn', '关灯', 'off', '0', 0, NULL),
	(25, 'common', '闹钟铃声音量范围', 'alarmVoiceLevelRange', '最小值', 'min', '1', 0, '闹钟不允许静音'),
	(26, 'common', '闹钟铃声音量范围', 'alarmVoiceLevelRange', '最大值', 'max', '5', 0, NULL),
	(27, 'common', '闹钟开关状态', 'alarmStatus', '开', 'on', '1', 1, NULL),
	(28, 'common', '闹钟开关状态', 'alarmStatus', '开', 'off', '0', 0, NULL),
	(29, 'common', '手机系统', 'mobileSystem', '安卓', 'android', '1', 1, NULL),
	(30, 'common', '手机系统', 'mobileSystem', '安卓', 'ios', '2', 0, NULL),
	(31, 'common', '聊天室者类型', 'chatterType', 'APP', 'app', '2', 0, NULL),
	(32, 'common', '聊天室者类型', 'chatterType', '台灯', 'lamp', '1', 1, NULL),
	(33, 'common', '聊天室消息类型', 'chatMsgType', '语音', 'voice', '1', 0, NULL),
	(34, 'common', '聊天室消息类型', 'chatMsgType', '文本', 'text', '2', 0, NULL),
	(35, 'common', '聊天室资源获取方式', 'chatResType', '本地文件', 'localFile', '1', 0, NULL),
	(36, 'common', '聊天室消息已读状态', 'chatMsgReadStatus', '已读', 'read', '1', 0, NULL),
	(37, 'common', '聊天室消息已读状态', 'chatMsgReadStatus', '未读', 'unread', '0', 0, NULL),
	(40, 'common', '设备是否需要更新', 'deviceUpdateNeed', '强制更新', 'force', '2', 0, NULL),
	(42, 'common', '设备是否需要更新', 'deviceUpdateNeed', '无需更新', 'noNeed', '0', 0, NULL),
	(43, 'common', '设备是否需要更新', 'deviceUpdateNeed', '可选更新', 'optional', '1', 0, NULL),
	(44, 'common', '设备是否需要更新', 'deviceUpdateNeed', '公测版本', 'beta', '4', 0, NULL),
	(45, 'common', '设备激活状态', 'deviceActiveStatus', '激活', 'active', '1', 0, NULL),
	(47, 'common', '设备激活状态', 'deviceActiveStatus', '未激活', 'inactive', '0', 0, NULL),
	(48, 'common', 'app广告条状态', 'appBannerStatus', '展示中', 'displayOn', '1', 0, NULL),
	(49, 'common', 'app广告条状态', 'appBannerStatus', '下架', 'displayOff', '0', 0, NULL),
	(50, 'common', 'app广告条状态', 'appBannerStatus', '删除', 'del', '-1', 0, NULL),
	(51, 'common', 'app广告条展示位置', 'appBannerDisplaySpot', '删除', 'apphometop', 'apphometop', 0, 'app首页顶部'),
	(52, 'common', 'app是否需要更新', 'appUpdateAvailable', '可更新', 'available', '1', 0, ''),
	(53, 'common', 'app是否需要更新', 'appUpdateAvailable', '不可更新', 'inavailable', '0', 0, ''),
	(54, 'common', '设备绑定状态', 'deviceBoundStatus', '绑定', 'bound', '1', 0, ''),
	(55, 'common', '设备绑定状态', 'deviceBoundStatus', '解绑', 'unbound', '-1', 0, ''),
	(56, 'common', 'mqtt读写标志', 'mqttAclsRW', '只读', 'r', '1', 0, NULL),
	(57, 'common', 'mqtt读写标志', 'mqttAclsRW', '只写', 'w', '2', 1, NULL),
	(58, 'common', 'mqtt读写标志', 'mqttAclsRW', '读写', 'rw', '3', 2, NULL),
	(59, 'common', '坐姿是否正确', 'positionCorrect', '不正确', 'incorrect', '-1', 0, NULL),
	(60, 'common', '坐姿是否正确', 'positionCorrect', '无人', 'notDetected', '0', 0, NULL),
	(61, 'common', '坐姿是否正确', 'positionCorrect', '正确', 'correct', '1', 0, NULL),
	(62, 'common', '反馈状态', 'feedbackStatus', '提交', 'post', '0', 0, NULL),
	(64, 'common', '反馈状态', 'feedbackStatus', '已经处理', 'handled', '1', 0, NULL);
/*!40000 ALTER TABLE `data_dict` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
