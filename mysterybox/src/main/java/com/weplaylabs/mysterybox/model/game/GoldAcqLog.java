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
@Table(name = "tb_gold_acq_log")
@EnableJpaRepositories
@IdClass(GoldAcqLogPK.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class GoldAcqLog
{
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "log_date")
    private long logDate;

    @Column(name = "add_value")
    private int addValue;

    @Column(name = "total_value")
    private long totalValue;

    @Builder(toBuilder = true)
    public GoldAcqLog(int userId, long logDate, int addValue, long totalValue)
    {
        this.userId = userId;
        this.logDate = logDate;
        this.addValue = addValue;
        this.totalValue = totalValue;
    }
}