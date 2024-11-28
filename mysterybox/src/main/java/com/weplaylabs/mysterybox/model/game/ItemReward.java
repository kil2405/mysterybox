package com.weplaylabs.mysterybox.model.game;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * tb_item_reward 테이블과 매핑 객체
 * 
 * @auther dgkim
 * @filename ItemReward
 * @date 2024.10.10
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_item_reward")
@EnableJpaRepositories
@IdClass(ItemRewardPK.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemReward
{
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "appear_num")
    private int appearNum;

    @Column(name = "count")
    private long count;

    @Column(name = "is_reward")
    private boolean isReward;

    @Builder(toBuilder = true)
    public ItemReward(int userId, int appearNum, long count, boolean isReward)
    {
        this.userId = userId;
        this.appearNum = appearNum;
        this.count = count;
        this.isReward = isReward;
    }
}
