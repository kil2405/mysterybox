package com.weplaylabs.mysterybox.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.game.GlobalRanking;

public interface GlobalRankingRepository extends JpaRepository<GlobalRanking, String> {
}
