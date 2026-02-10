package com.pfe.production.repository;

import com.pfe.production.domain.ProductionJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionJobRepository extends MongoRepository<ProductionJob, String> {
    ProductionJob findByOrderId(String orderId);

    List<ProductionJob> findByStatus(String status);
}
