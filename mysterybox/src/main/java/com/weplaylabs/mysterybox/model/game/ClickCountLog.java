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
@Table(name = "tb_click_count_log")
@EnableJpaRepositories
@IdClass(ClickCountLogPK.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClickCountLog
{
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "log_date")
    private long logDate;

    @Column(name = "click_count")
    private long clickCount;

    @Column(name = "total_count")
    private long totalCount;

    @Builder(toBuilder = true)
    public ClickCountLog(int userId, long logDate, long clickCount, long totalCount)
    {
        this.userId = userId;
        this.logDate = logDate;
        this.clickCount = clickCount;
        this.totalCount = totalCount;
    }
}