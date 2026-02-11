package com.pfe.production.domain.palette;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPaletteRepository extends MongoRepository<JobPalette, String> {
    Optional<JobPalette> findByPaletteNumber(String paletteNumber);
}