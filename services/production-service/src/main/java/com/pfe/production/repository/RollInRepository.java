package com.pfe.production.repository;

import com.pfe.production.domain.RollIn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RollInRepository extends MongoRepository<RollIn, String> {
    Optional<RollIn> findByRollNumber(String rollNumber);

    List<RollIn> findByQualityCodeAndStatus(String qualityCode, RollIn.RollInStatus status);

    Optional<RollIn> findFirstByQualityCodeAndStatusAndWidthGreaterThanEqualAndLengthGreaterThanEqual(
            String qualityCode, RollIn.RollInStatus status, Double width, Double length);
}
