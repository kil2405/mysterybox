package com.weplaylabs.mysterybox.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.resource.DailyItemAppearResource;

public interface DailyItemAppearRepository extends JpaRepository<DailyItemAppearResource, Integer> {
    
}
