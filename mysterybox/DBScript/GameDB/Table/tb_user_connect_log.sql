USE mysterybox;

DROP TABLE IF EXISTS `tb_user_connect_log`;

CREATE TABLE `tb_user_connect_log` (
  `user_id` INT(11) NOT NULL COMMENT '유저의 고유값',
  `log_date` BIGINT(20) NOT NULL COMMENT '로그시간',
  `click_count` BIGINT(20) NOT NULL COMMENT '로그인 시점 클릭 카운트',
  `gold` BIGINT(20) NOT NULL COMMENT '로그인 시점 보유 골드',
  `client_ip` VARCHAR(64) NOT NULL COMMENT '클라이언트 IP',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 업데이트 시간',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '최초 생성 시간',
  PRIMARY KEY (`user_id`, `log_date`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='유저 로그인 로그'