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
@Table(name = "tb_daily_item_appear")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DailyItemAppearResource implements Serializable
{
    @Id
    @Column(name = "index")
    private int index;

    @Column(name = "appear_num")
    private int appearNum;

    @Column(name = "min")
    private int min;

    @Column(name = "max")
    private int max;

    @Column(name = "effect_prefab")
    private String effectPrefab;

    @Column(name = "item_group")
    private int itemGroup;

    @Builder(toBuilder = true)
    public DailyItemAppearResource(int index, int appearNum, int min, int max, String effectPrefab, int itemGroup)
    {
        this.index = index;
        this.appearNum = appearNum;
        this.min = min;
        this.max = max;
        this.effectPrefab = effectPrefab;
        this.itemGroup = itemGroup;
    }
}
