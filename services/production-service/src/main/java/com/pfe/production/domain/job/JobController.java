package com.pfe.production.domain.job;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/production/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        Job job = jobService.createJob(request);
        return ResponseEntity.ok(JobResponse.fromEntity(job));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<JobResponse> startJob(@PathVariable String id) {
        try {
            return ResponseEntity.ok(JobResponse.fromEntity(jobService.startJob(id)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/steps/{stepName}/start")
    public ResponseEntity<JobResponse> startStep(@PathVariable String id, @PathVariable String stepName) {
        try {
            return ResponseEntity.ok(JobResponse.fromEntity(jobService.startStep(id, stepName)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/next-step")
    public ResponseEntity<JobResponse> startNextStep(@PathVariable String id,
            @RequestBody(required = false) Map<String, String> payload) {
        try {
            String machineId = (payload != null) ? payload.get("machineId") : null;
            return ResponseEntity.ok(JobResponse.fromEntity(jobService.startNextStep(id, machineId)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/steps/{stepName}/complete")
    public ResponseEntity<JobResponse> completeStep(
            @PathVariable String id,
            @PathVariable String stepName,
            @RequestBody Map<String, Object> payload) {
        try {
            Integer actualQuantity = payload.containsKey("actualQuantity") ? (Integer) payload.get("actualQuantity")
                    : null;
            String notes = payload.containsKey("notes") ? (String) payload.get("notes") : null;

            return ResponseEntity
                    .ok(JobResponse.fromEntity(jobService.completeStep(id, stepName, actualQuantity, notes)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable String id) {
        return jobService.getJobById(id)
                .map(JobResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/version/{formVersionId}")
    public ResponseEntity<List<JobResponse>> getJobsByVersion(@PathVariable String formVersionId) {
        List<JobResponse> jobs = jobService.getJobsByFormVersion(formVersionId).stream()
                .map(JobResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobs);
    }
}