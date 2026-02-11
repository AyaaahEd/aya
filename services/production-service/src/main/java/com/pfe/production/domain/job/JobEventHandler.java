package com.pfe.production.domain.job;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(JobEventHandler.class);

    @EventHandler
    public void on(JobCreatedEvent event) {
        logger.info("[AXON EVENT] Job Created: {} (Number: {})", event.getJobId(), event.getJobNumber());
        // Here you could update a read model, send a notification, etc.
    }
}