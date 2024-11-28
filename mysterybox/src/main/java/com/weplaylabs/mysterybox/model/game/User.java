package com.weplaylabs.mysterybox.model.game;

import java.io.Serializable;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * tb_user 테이블과 매핑 객체
 * 
 * @auther dgkim
 * @filename UsereEntity
 * @date 2024.08.29
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_user")
@EnableJpaRepositories
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_uid")
    private String userUid;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "encryption")
    private String encryption;

    @Column(name = "wallet")
    private String wallet;

    @Column(name = "terms_agree")
    private boolean termsAgree;

    @Column(name = "user_grade")
    private byte userGrade;

    @Column(name = "gold")
    private long gold;

    @Column(name = "click_count")
    private long clickCount;

    @Column(name = "is_guest")
    private byte isGuest;

    @Column(name = "language")
    private String language;

    @Column(name = "region")
    private String region;

    @Column(name = "login_time")
    private long loginTime;

    @Builder(toBuilder = true)
    public User(int userId, String userUid, String userNickname, String encryption, String wallet, boolean termsAgree, byte userGrade, long gold, long clickCount, byte isGuest, String language, String region, long loginTime)
    {
        this.userId = userId;
        this.userUid = userUid;
        this.userNickname = userNickname;
        this.encryption = encryption;
        this.wallet = wallet;
        this.termsAgree = termsAgree;
        this.userGrade = userGrade;
        this.gold = gold;
        this.clickCount = clickCount;
        this.isGuest = isGuest;
        this.language = language;
        this.region = region;
        this.loginTime = loginTime;
    }
}