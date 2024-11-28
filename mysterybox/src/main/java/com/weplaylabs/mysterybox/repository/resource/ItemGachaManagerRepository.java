package com.weplaylabs.mysterybox.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.resource.ItemGachaManagerResource;

public interface ItemGachaManagerRepository extends JpaRepository<ItemGachaManagerResource, Integer> {
    
}
