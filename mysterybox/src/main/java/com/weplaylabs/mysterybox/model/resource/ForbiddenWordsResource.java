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
@Table(name = "tb_forbidden_words")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ForbiddenWordsResource implements Serializable
{
    @Id
    @Column(name = "index")
    private int index;

    @Column(name = "text")
    private String text;

    @Builder(toBuilder = true)
    public ForbiddenWordsResource(int index, String text)
    {
        this.index = index;
        this.text = text;
    }
}
