package com.weplaylabs.mysterybox.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.resource.ErrorCodeResource;

public interface ErrorCodeRepository extends JpaRepository<ErrorCodeResource, Integer> {
    
}
