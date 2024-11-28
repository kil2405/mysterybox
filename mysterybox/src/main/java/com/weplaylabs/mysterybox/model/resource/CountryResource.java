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
 * tb_country 테이블과 매핑 객체
 * 
 * @auther dgkim
 * @filename TableCountry DataTable
 * @date 2024.10.08
 */

@Getter
@ToString
@Entity
@Table(name = "tb_country")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CountryResource implements Serializable
{
    @Id
    @Column(name = "index")
    private int index;

    @Column(name = "country_id")
    private int countryId;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "id_text_name")
    private int idTextName;

    @Column(name = "flag_image")
    private String flagImage;

    @Column(name = "annotation_name")
    private String annotationName;

    @Builder(toBuilder = true)
    public CountryResource(int index, int countryId, String countryCode, int idTextName, String flagImage, String annotationName)
    {
        this.index = index;
        this.countryId = countryId;
        this.countryCode = countryCode;
        this.idTextName = idTextName;
        this.flagImage = flagImage;
        this.annotationName = annotationName;
    }
}
