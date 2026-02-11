package com.pfe.production.repository;

import com.pfe.production.domain.FormVersion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormVersionRepository extends MongoRepository<FormVersion, String> {
    List<FormVersion> findByFormId(String formId);

    long countByFormId(String formId);

    Optional<FormVersion> findByFormIdAndVersionNumber(String formId, Integer versionNumber);

    @org.springframework.data.mongodb.repository.Query("{ 'planning.machineId': ?0, 'planning.startDate': { $lt: ?2 }, 'planning.endDate': { $gt: ?1 } }")
    boolean existsOverlappingPlanning(String machineId, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
