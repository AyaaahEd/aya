package com.pfe.production.repository;

import com.pfe.production.domain.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends MongoRepository<Machine, String> {
    Optional<Machine> findByName(String name);

    List<Machine> findByProcessing(Machine.MachineProcessing processing);

    List<Machine> findByProcessingAndStatus(Machine.MachineProcessing processing, Machine.MachineStatus status);
}
