package com.weplaylabs.mysterybox.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.resource.ItemListResource;

public interface ItemListRepository extends JpaRepository<ItemListResource, Integer> {
    
}
