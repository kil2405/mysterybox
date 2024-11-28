USE mysterybox;

DROP TABLE IF EXISTS `tb_user_item`;

CREATE TABLE `tb_user_item` (
  `user_id` INT(11) NOT NULL COMMENT '유저의 고유값',
  `item_id` INT(11) NOT NULL COMMENT '아이템 ID',
  `count` BIGINT(20) NOT NULL COMMENT '아이템 개수',
  `is_equip` bit(1) NOT NULL COMMENT '아이템 장착여부 (true : 장착, false : 미장착)',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 업데이트 시간',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '최초 생성 시간',
  PRIMARY KEY (`user_id`, `item_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='유저 아이템 정보'