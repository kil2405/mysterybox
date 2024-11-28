package com.weplaylabs.mysterybox.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.weplaylabs.mysterybox.model.game.User;


public interface UserJpaRepository extends JpaRepository<User, Integer> {
    User findByUserId(int userId);

    User findByUserUid(String userUid);

    User findByUserNickname(String userNickname);
    
    //일반 SQL Query
    @Query(value = "SELECT UNIX_TIMESTAMP(NOW())", nativeQuery = true)
    long getNowUnixTime();
}
