package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.game.ClickCountLog;
import com.weplaylabs.mysterybox.model.game.ClickCountLogPK;

public interface ClickCountLogRepository extends JpaRepository<ClickCountLog, ClickCountLogPK> {
    //UserId로 조회 하는 메서드
    List<ClickCountLog> findAllByUserId(int userId);   
}
