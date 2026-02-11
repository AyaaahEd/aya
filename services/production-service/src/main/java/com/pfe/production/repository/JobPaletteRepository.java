package com.pfe.production.repository;

import com.pfe.production.domain.JobPalette;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPaletteRepository extends MongoRepository<JobPalette, String> {
    Optional<JobPalette> findByPaletteNumber(String paletteNumber);
}
