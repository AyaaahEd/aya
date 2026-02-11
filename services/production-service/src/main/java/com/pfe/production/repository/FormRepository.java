package com.pfe.production.repository;

import com.pfe.production.domain.Form;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormRepository extends MongoRepository<Form, String> {
    Optional<Form> findByFormNumber(String formNumber);

    boolean existsByFormNumber(String formNumber);
}
