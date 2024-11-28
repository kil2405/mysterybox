package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.game.GoldAcqLog;
import com.weplaylabs.mysterybox.model.game.GoldAcqLogPK;

public interface GoldAcqLogRepository extends JpaRepository<GoldAcqLog, GoldAcqLogPK> {
    //UserId로 조회 하는 메서드
    List<GoldAcqLog> findAllByUserId(int userId);
}
