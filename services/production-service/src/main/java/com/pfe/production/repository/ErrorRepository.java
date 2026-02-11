package com.pfe.production.repository;

import com.pfe.production.domain.ErrorEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorRepository extends MongoRepository<ErrorEntry, String> {
    List<ErrorEntry> findByEntityTypeAndEntityId(String entityType, String entityId);

    List<ErrorEntry> findByStatus(ErrorEntry.ErrorStatus status);
}
