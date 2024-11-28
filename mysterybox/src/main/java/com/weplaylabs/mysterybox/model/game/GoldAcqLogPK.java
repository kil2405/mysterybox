package com.weplaylabs.mysterybox.model.game;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoldAcqLogPK implements Serializable
{
    private int userId;
    private long logDate;

    public GoldAcqLogPK() {}

    public GoldAcqLogPK(int userId, long logDate)
    {
        this.userId = userId;
        this.logDate = logDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoldAcqLogPK that = (GoldAcqLogPK) o;
        return userId == that.userId && logDate == that.logDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, logDate);
    }
}
