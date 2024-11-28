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
@Table(name = "tb_item_list")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemListResource implements Serializable
{
    @Id
    @Column(name = "index")
    private int index;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "id_text_name")
    private int idTextName;

    @Column(name = "id_text_desc")
    private int idTextDesc;

    @Column(name = "item_prefab")
    private String itemPrefab;

    @Column(name = "appear_location")
    private String appearLocation;

    @Column(name = "touch_effect")
    private String touchEffect;

    @Column(name = "item_bg")
    private String itemBg;

    @Column(name = "annotation_name")
    private String annotationName;

    @Column(name = "annotation_desc")
    private String annotationDesc;

    @Builder(toBuilder = true)
    public ItemListResource(int index, int itemId, int idTextName, int idTextDesc, String itemPrefab, String appearLocation, String touchEffect, String itemBg, String annotationName, String annotationDesc)
    {
        this.index = index;
        this.itemId = itemId;
        this.idTextName = idTextName;
        this.idTextDesc = idTextDesc;
        this.itemPrefab = itemPrefab;
        this.appearLocation = appearLocation;
        this.touchEffect = touchEffect;
        this.itemBg = itemBg;
        this.annotationName = annotationName;
        this.annotationDesc = annotationDesc;
    }
}
