package com.pfe.production.domain.job;

import java.time.LocalDateTime;

public class JobCreatedEvent {
    private final String jobId;
    private final String jobNumber;
    private final String orderItemId;
    private final LocalDateTime createdAt;

    public JobCreatedEvent(String jobId, String jobNumber, String orderItemId, LocalDateTime createdAt) {
        this.jobId = jobId;
        this.jobNumber = jobNumber;
        this.orderItemId = orderItemId;
        this.createdAt = createdAt;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
