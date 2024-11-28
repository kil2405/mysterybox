package com.weplaylabs.mysterybox.model.game;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClickCountLogPK implements Serializable
{
    private int userId;
    private long logDate;

    public ClickCountLogPK() {}

    public ClickCountLogPK(int userId, long logDate)
    {
        this.userId = userId;
        this.logDate = logDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickCountLogPK that = (ClickCountLogPK) o;
        return userId == that.userId && logDate == that.logDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, logDate);
    }
}
