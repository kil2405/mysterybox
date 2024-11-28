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
 * tb_user_item 테이블과 매핑 객체
 * 
 * @auther dgkim
 * @filename UserItem
 * @date 2024.10.11
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_user_item")
@EnableJpaRepositories
@IdClass(UserItemPK.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserItem
{
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "count")
    private long count;

    @Column(name = "is_equip")
    private boolean isEquip;

    @Builder(toBuilder = true)
    public UserItem(int userId, int itemId, long count, boolean isEquip)
    {
        this.userId = userId;
        this.itemId = itemId;
        this.count = count;
        this.isEquip = isEquip;
    }
}
