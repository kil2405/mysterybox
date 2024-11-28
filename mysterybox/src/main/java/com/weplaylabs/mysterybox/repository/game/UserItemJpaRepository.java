package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.weplaylabs.mysterybox.model.game.UserItem;
import com.weplaylabs.mysterybox.model.game.UserItemPK;

public interface UserItemJpaRepository extends JpaRepository<UserItem, UserItemPK> {
    //UserId로 조회 하는 메서드
    List<UserItem> findAllByUserId(int userId);

    // 일반 Sql Query
    @Query(value = "SELECT COUNT(*) FROM tb_user_item WHERE user_id = ?1", nativeQuery = true)
    int countByUserId(int userId);
}
