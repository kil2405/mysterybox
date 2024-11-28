package com.weplaylabs.mysterybox.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.game.RewardGold;

public interface RewardGoldRepository extends JpaRepository<RewardGold, Integer> {
    RewardGold findByUserId(int userId);
}
