-- ----------------------------
-- Table structure for audit_log
-- ----------------------------
DROP TABLE IF EXISTS `audit_log`;
CREATE TABLE `audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `class_method` varchar(128) DEFAULT NULL COMMENT '请求方法',
  `class_method_args` varchar(256) DEFAULT NULL COMMENT '请求参数',
  `class_method_return` varchar(1024) DEFAULT NULL COMMENT '请求返回类型',
  `client_ip` varchar(32) DEFAULT NULL COMMENT '发送请求的客户端ip 地址',
  `http_method` varchar(64) DEFAULT NULL COMMENT 'http 请求方法',
  `http_uri` varchar(256) DEFAULT NULL COMMENT 'http 请求路径',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(32) DEFAULT NULL COMMENT '登录后的用户名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作审计表';

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `name` varchar(255) DEFAULT NULL COMMENT '契约名称',
  `status` int(10) NOT NULL COMMENT '契约状态，枚举类型，包括：创建、维护、发布、删除',
  `org_id` bigint(20) NOT NULL COMMENT '契约所属组织',
  `content` text NOT NULL COMMENT '契约具体内容',
  `description` varchar(255) DEFAULT NULL COMMENT '契约描述信息',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '插入时间，即上传契约的时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更改时间',
  `insert_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改者',
  `isactive` tinyint(1) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
