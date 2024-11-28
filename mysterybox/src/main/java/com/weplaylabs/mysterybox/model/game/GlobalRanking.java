package com.weplaylabs.mysterybox.model.game;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * tb_global_ranking 테이블과 매핑 객체
 * 
 * @auther dgkim
 * @filename globalRanking
 * @date 2024.10.14
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_global_ranking")
@EnableJpaRepositories
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class GlobalRanking
{
    @Id
    @Column(name = "region")
    private String region;

    @Column(name = "count")
    private long count;

    @Builder(toBuilder = true)
    public GlobalRanking(String region, long count)
    {
        this.region = region;
        this.count = count;
    }
}
