package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.game.ItemAcqLog;
import com.weplaylabs.mysterybox.model.game.ItemAcqLogPK;

public interface ItemAcqLogRepository extends JpaRepository<ItemAcqLog, ItemAcqLogPK> {
    //UserId로 조회 하는 메서드
    List<ItemAcqLog> findAllByUserId(int userId);
}
