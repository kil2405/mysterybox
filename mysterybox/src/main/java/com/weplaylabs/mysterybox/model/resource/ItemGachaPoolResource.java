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
@Table(name = "tb_item_gacha_pool")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemGachaPoolResource implements Serializable
{
    @Id
    @Column(name = "index")
    private int index;

    @Column(name = "gachapool")
    private int gachapool;

    @Column(name = "id_item")
    private int idItem;

    @Column(name = "min")
    private int min;

    @Column(name = "max")
    private int max;

    @Column(name = "prob")
    private int prob;

    @Builder(toBuilder = true)
    public ItemGachaPoolResource(int index, int gachapool, int idItem, int min, int max, int prob)
    {
        this.index = index;
        this.gachapool = gachapool;
        this.idItem = idItem;
        this.min = min;
        this.max = max;
        this.prob = prob;
    }
}
