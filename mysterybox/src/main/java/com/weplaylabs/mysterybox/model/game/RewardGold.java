package com.weplaylabs.mysterybox.model.game;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * tb_reward_gold 테이블과 매핑 객체
 * 
 * @auther dgkim
 * @filename RewardGold
 * @date 2024.10.15
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_reward_gold")
@EnableJpaRepositories
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RewardGold
{
    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "gold")
    private int gold;

    @Column(name = "reward_time")
    private long rewardTime;

    @Builder(toBuilder = true)
    public RewardGold(int userId, int gold, long rewardTime)
    {
        this.userId = userId;
        this.gold = gold;
        this.rewardTime = rewardTime;
    }
}
