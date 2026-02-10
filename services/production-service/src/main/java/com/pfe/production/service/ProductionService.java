package com.pfe.production.service;

import com.pfe.production.domain.ProductionJob;
import com.pfe.production.domain.ProductionStep;
import com.pfe.production.repository.ProductionJobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductionService {

    private final ProductionJobRepository productionJobRepository;
    private final WorkflowEngine workflowEngine;
    private final WebClient webClient;

    public ProductionService(ProductionJobRepository productionJobRepository, WorkflowEngine workflowEngine,
            WebClient.Builder webClientBuilder) {
        this.productionJobRepository = productionJobRepository;
        this.workflowEngine = workflowEngine;
        this.webClient = webClientBuilder.build();
    }

    public ProductionJob createJob(ProductionJob job) {
        // Use WorkflowEngine to determine steps based on job details (dummy logic for
        // now, utilizing the engine)
        // In a real app, we'd inspect the order items to decide steps.
        // For now, let's just generate a standard workflow.
        List<ProductionStep> steps = workflowEngine.getWorkflowForOrder("Standard", false);

        job.setSteps(steps);
        job.setCreatedAt(LocalDateTime.now());
        job.setStatus("QUEUED");

        return productionJobRepository.save(job);
    }

    public List<ProductionJob> getAllJobs() {
        return productionJobRepository.findAll();
    }

    public ProductionJob completeJob(String jobId) {
        ProductionJob job = productionJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus("COMPLETED");
        job.setCompletedAt(LocalDateTime.now());
        ProductionJob savedJob = productionJobRepository.save(job);

        // Notify Order Service (Sync)
        try {
            webClient.put()
                    .uri("http://order-service:8083/api/orders/" + job.getOrderId() + "/status")
                    .bodyValue("SHIPPED")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();
        } catch (Exception e) {
            System.err.println("Failed to update Order status: " + e.getMessage());
        }

        return savedJob;
    }
}
