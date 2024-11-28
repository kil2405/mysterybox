package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weplaylabs.mysterybox.model.game.UserItem;
import com.weplaylabs.mysterybox.model.game.UserItemPK;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserItemJpaService
{
    private final UserItemJpaRepository userItemJpaRepository;

    public List<UserItem> findAllByUserId(int userId)
    {
        return userItemJpaRepository.findAllByUserId(userId);
    }

    public List<UserItem> saveAll(List<UserItem> userItems)
    {
        return userItemJpaRepository.saveAll(userItems);
    }

    public UserItem save(UserItem userItem)
    {
        return userItemJpaRepository.save(userItem);
    }

    public UserItem findUserItem(int userId, int itemId)
    {
        return userItemJpaRepository.findById(new UserItemPK(userId, itemId)).orElse(null);
    }

    public int countByUserId(int userId)
    {
        return userItemJpaRepository.countByUserId(userId);
    }
}
