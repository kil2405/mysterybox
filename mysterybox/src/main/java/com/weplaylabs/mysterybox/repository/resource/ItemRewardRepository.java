package com.weplaylabs.mysterybox.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.resource.ItemRewardResource;

public interface ItemRewardRepository extends JpaRepository<ItemRewardResource, Integer> {
    
}
