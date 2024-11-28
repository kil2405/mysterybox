package com.weplaylabs.mysterybox.model.game;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRewardPK implements Serializable
{
    private int userId;
    private int appearNum;

    public ItemRewardPK() {}

    public ItemRewardPK(int userId, int appearNum)
    {
        this.userId = userId;
        this.appearNum = appearNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRewardPK that = (ItemRewardPK) o;
        return userId == that.userId && appearNum == that.appearNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, appearNum);
    }
}
