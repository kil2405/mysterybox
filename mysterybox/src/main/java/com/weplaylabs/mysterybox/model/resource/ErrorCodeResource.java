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

/**
 * tb_user 테이블과 매핑 객체
 * 
 * @auther dgkim
 * @filename UsereEntity
 * @date 2024.08.29
 */

 @Getter
 @ToString
 @Entity
 @Table(name = "tb_error_code")
 @NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorCodeResource implements Serializable
{
    @Id
    @Column(name = "error_code")
    private int errorCode;

    @Column(name = "text_kr")
    private String textKr;

    @Column(name = "text_en")
    private String textEn;

    @Builder(toBuilder = true)
    public ErrorCodeResource(int errorCode, String textKr, String textEn)
    {
        this.errorCode = errorCode;
        this.textKr = textKr;
        this.textEn = textEn;
    }
}
