package com.weplaylabs.mysterybox.repository.game;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weplaylabs.mysterybox.model.game.ItemReward;
import com.weplaylabs.mysterybox.model.game.ItemRewardPK;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemRewardJpaService
{
    private final ItemRewardJpaRepository itemRewardJpaRepository;

    public List<ItemReward> findAllByUserId(int userId)
    {
        return itemRewardJpaRepository.findAllByUserId(userId);
    }

    public List<ItemReward> saveAll(List<ItemReward> itemRewards)
    {
        return itemRewardJpaRepository.saveAll(itemRewards);
    }

    public ItemReward save(ItemReward itemReward)
    {
        return itemRewardJpaRepository.save(itemReward);
    }

    public ItemReward findRewardItem(int userId, int appearNum)
    {
        return itemRewardJpaRepository.findById(new ItemRewardPK(userId, appearNum)).orElse(null);
    }
}
