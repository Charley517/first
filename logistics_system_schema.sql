-- 先删除表，避免重复建表报错
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `courier`;
DROP TABLE IF EXISTS `user`;

-- 用户表
CREATE TABLE user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100),
  phone VARCHAR(20),
  role ENUM('admin', 'sender', 'receiver', 'courier') NOT NULL DEFAULT 'sender',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 配送员表
CREATE TABLE courier (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  phone VARCHAR(20),
  current_location VARCHAR(255),
  performance_score DOUBLE DEFAULT 0,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE `order` (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  courier_id BIGINT,
  weight DOUBLE NOT NULL,
  distance DOUBLE NOT NULL,
  fee DOUBLE NOT NULL,
  status ENUM('已揽件', '运输中', '已签收', '已取消') DEFAULT '已揽件',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  sender_name VARCHAR(50),
  sender_phone VARCHAR(20),
  sender_address VARCHAR(255),
  receiver_name VARCHAR(50),
  receiver_phone VARCHAR(20),
  receiver_address VARCHAR(255),
  CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES user(id),
  CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES user(id),
  CONSTRAINT fk_courier FOREIGN KEY (courier_id) REFERENCES courier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 索引优化
CREATE INDEX idx_order_status ON `order` (status);
CREATE INDEX idx_order_created_time ON `order` (created_time);
