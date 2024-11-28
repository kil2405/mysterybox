USE mysterybox;

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '유저의 고유값',
  `user_uid` VARCHAR(128) NOT NULL COMMENT '게임베이스 고유값',
  `user_nickname` VARCHAR(25) DEFAULT NULL COMMENT '닉네임',
  `encryption` VARCHAR(255) NOT NULL COMMENT '암호화 키값',
  `wallet` VARCHAR(255) NOT NULL COMMENT '지갑주소',
  `terms_agree` bit(1) NOT NULL COMMENT '약관 동의 체크',
  `user_grade` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '유저등급 : 0:운영자, 1:일반유저, 2:블럭유저',
  `gold` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '보유골드',
  `click_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '하루 클릭 카운트',
  `is_guest` tinyint(4) NOT NULL DEFAULT 0 COMMENT '게스트 로그인 여부',
  `language` VARCHAR(64) DEFAULT NULL COMMENT '사용 언어',
  `region` VARCHAR(8) DEFAULT NULL COMMENT '접속 지역',
  `login_time` int(11) NOT NULL COMMENT '로그인 시각',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 업데이트 시간',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '최초 생성 시간',
  PRIMARY KEY (`user_id`),
  UNIQUE (`user_uid`, user_nickname)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='유저의 게임 정보'