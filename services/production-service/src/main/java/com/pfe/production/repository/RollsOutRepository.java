package com.pfe.production.repository;

import com.pfe.production.domain.RollsOut;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RollsOutRepository extends MongoRepository<RollsOut, String> {
    Optional<RollsOut> findByRollsOutNumber(String rollsOutNumber);

    List<RollsOut> findByFormVersionId(String formVersionId);
}
