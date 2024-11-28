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
@Table(name = "tb_item_gacha_manage")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemGachaManagerResource implements Serializable
{
    @Id
    @Column(name = "index")
    private int index;

    @Column(name = "id_season")
    private int idSeason;

    @Column(name = "item_group")
    private int itemGroup;

    @Column(name = "id_gachapool")
    private int idGachaPool;

    @Column(name = "prob")
    private int prob;

    @Builder(toBuilder = true)
    public ItemGachaManagerResource(int index, int idSeason, int itemGroup, int idGachaPool, int prob)
    {
        this.index = index;
        this.idSeason = idSeason;
        this.itemGroup = itemGroup;
        this.idGachaPool = idGachaPool;
        this.prob = prob;
    }
}
