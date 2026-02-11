package com.pfe.production.repository;

import com.pfe.production.domain.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    Optional<Job> findByJobNumber(String jobNumber);

    List<Job> findByFormVersionId(String formVersionId);

    List<Job> findByOrderItemId(String orderItemId);
}
