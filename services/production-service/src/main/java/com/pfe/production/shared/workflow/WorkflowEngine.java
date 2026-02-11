package com.pfe.production.shared.workflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.production.domain.production.ProductionStep;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class WorkflowEngine {

    private List<WorkflowConfig> workflowConfigs;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("workflows.json");
            workflowConfigs = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
            });
            System.out.println("Loaded " + workflowConfigs.size() + " workflow configurations.");
        } catch (IOException e) {
            e.printStackTrace();
            workflowConfigs = Collections.emptyList();
        }
    }

    public List<ProductionStep> getWorkflowForOrder(String quality, boolean hasBorder) {
        if (workflowConfigs == null)
            return Collections.emptyList();

        for (WorkflowConfig config : workflowConfigs) {
            for (Combination combination : config.getCombinations()) {
                if (combination.getQuality().equals(quality) && combination.isBorder() == hasBorder) {
                    // Return a deep copy or new list of steps to ensure fresh state for each order
                    return config.getSteps().stream()
                            .map(s -> new ProductionStep(s.getMachineId(), s.getMachineName(), s.getType(),
                                    s.getPriority()))
                            .toList();
                }
            }
        }

        // Default Fallback
        return List.of(
                new ProductionStep("default-colaris", "Colaris", "form", 1),
                new ProductionStep("default-coating", "Coating", "form", 2),
                new ProductionStep("default-cutting", "Cutting", "form", 3));
    }

    // Helper classes for JSON mapping
    // Helper classes for JSON mapping
    public static class WorkflowConfig {
        private List<ProductionStep> steps;
        private List<Combination> combinations;

        public List<ProductionStep> getSteps() {
            return steps;
        }

        public void setSteps(List<ProductionStep> steps) {
            this.steps = steps;
        }

        public List<Combination> getCombinations() {
            return combinations;
        }

        public void setCombinations(List<Combination> combinations) {
            this.combinations = combinations;
        }
    }

    public static class Combination {
        private String quality;
        private boolean border;

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public boolean isBorder() {
            return border;
        }

        public void setBorder(boolean border) {
            this.border = border;
        }
    }
}
