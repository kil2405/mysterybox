package com.weplaylabs.mysterybox.model.resource;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "tb_item_reward")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemRewardResource implements Serializable
{
    @Id
    @Column(name = "count")
    private int count;

    @Column(name = "gold_reward")
    private int goldReward;

    @Builder(toBuilder = true)
    public ItemRewardResource(int count, int goldReward)
    {
        this.count = count;
        this.goldReward = goldReward;
    }
}
