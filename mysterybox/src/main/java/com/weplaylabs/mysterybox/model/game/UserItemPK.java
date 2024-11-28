package com.weplaylabs.mysterybox.model.game;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserItemPK implements Serializable
{
    private int userId;
    private int itemId;

    public UserItemPK() {}

    public UserItemPK(int userId, int itemId)
    {
        this.userId = userId;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserItemPK that = (UserItemPK) o;
        return userId == that.userId && itemId == that.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, itemId);
    }
}
