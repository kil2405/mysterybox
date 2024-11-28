package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.game.UserConnectLog;
import com.weplaylabs.mysterybox.model.game.UserConnectLogPK;

public interface UserConnectLogRepository extends JpaRepository<UserConnectLog, UserConnectLogPK> {
    //UserId로 조회 하는 메서드
    List<UserConnectLog> findAllByUserId(int userId);
    
}
