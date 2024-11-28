package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.game.ItemReward;
import com.weplaylabs.mysterybox.model.game.ItemRewardPK;

public interface ItemRewardJpaRepository extends JpaRepository<ItemReward, ItemRewardPK> {
    //UserId로 조회 하는 메서드
    List<ItemReward> findAllByUserId(int userId);
}
