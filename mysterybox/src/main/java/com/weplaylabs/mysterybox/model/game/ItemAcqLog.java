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

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_item_acq_log")
@EnableJpaRepositories
@IdClass(ItemAcqLogPK.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ItemAcqLog
{
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "log_date")
    private long logDate;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "add_count")
    private int addCount;

    @Builder(toBuilder = true)
    public ItemAcqLog(int userId, long logDate, int itemId, int addCount)
    {
        this.userId = userId;
        this.logDate = logDate;
        this.itemId = itemId;
        this.addCount = addCount;
    }
}

