package com.pfe.production.domain.production;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/jobs")
public class ProductionController {

    private final com.pfe.production.domain.production.ProductionService productionService;

    public ProductionController(com.pfe.production.domain.production.ProductionService productionService) {
        this.productionService = productionService;
    }

    @PostMapping
    public ResponseEntity<ProductionJob> createJob(@RequestBody ProductionJob job) {
        ProductionJob savedJob = productionService.createJob(job);
        return ResponseEntity.ok(savedJob);
    }

    @GetMapping
    public List<ProductionJob> getAllJobs() {
        return productionService.getAllJobs();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ProductionJob> completeJob(@PathVariable String id) {
        return ResponseEntity.ok(productionService.completeJob(id));
    }
}