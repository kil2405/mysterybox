package com.weplaylabs.mysterybox.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weplaylabs.mysterybox.model.resource.CountryResource;

public interface CountryRepository extends JpaRepository<CountryResource, Integer> {
}
