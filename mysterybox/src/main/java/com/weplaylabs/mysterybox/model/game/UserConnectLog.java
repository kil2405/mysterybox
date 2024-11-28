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
@Table(name = "tb_user_connect_log")
@EnableJpaRepositories
@IdClass(UserConnectLogPK.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserConnectLog
{
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "log_date")
    private long logDate;

    @Column(name = "click_count")
    private long clickCount;

    @Column(name = "gold")
    private long gold;

    @Column(name = "client_ip")
    private String clientIp;

    @Builder(toBuilder = true)
    public UserConnectLog(int userId, long logDate, long clickCount, long gold, String clientIp)
    {
        this.userId = userId;
        this.logDate = logDate;
        this.clickCount = clickCount;
        this.gold = gold;
        this.clientIp = clientIp;
    }
}
