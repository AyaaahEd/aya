package com.pfe.production.domain.orderitem;
import com.pfe.production.shared.exception.BusinessException;
import com.pfe.production.shared.workflow.WorkflowEngine;
import com.pfe.production.shared.sequence.SequenceGeneratorService;
import com.pfe.production.domain.audit.AuditLogService;

import com.pfe.production.domain.production.ProductionStep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final WorkflowEngine workflowEngine;
    private final AuditLogService auditLogService;

    public OrderItemService(OrderItemRepository orderItemRepository, WorkflowEngine workflowEngine,
            AuditLogService auditLogService) {
        this.orderItemRepository = orderItemRepository;
        this.workflowEngine = workflowEngine;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public OrderItem createOrderItem(CreateOrderItemRequest request) {
        // Validate uniqueness
        if (orderItemRepository.findByItemId(request.itemId()).isPresent()) {
            throw new IllegalArgumentException("OrderItem with ID " + request.itemId() + " already exists");
        }

        OrderItem item = new OrderItem();
        item.setItemId(request.itemId());
        item.setOrderNumber(request.orderNumber());
        item.setThumbnail(request.thumbnail());
        item.setPromiseAvailableDate(request.promiseAvailableDate());
        item.setConfigurationProperties(request.configurationProperties() != null ? request.configurationProperties()
                : Collections.emptyList());
        item.setReprint(false);
        item.setState(OrderItem.OrderItemState.PENDING);
        item.setCreatedBy("system");
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        // Determine Workflow
        String quality = "500"; // default or extract from properties
        boolean border = false; // default or extract from properties

        if (item.getConfigurationProperties() != null) {
            for (OrderItem.ConfigurationProperty prop : item.getConfigurationProperties()) {
                if ("quality".equalsIgnoreCase(prop.getCode())) {
                    quality = prop.getLabel(); // Assuming label holds the value like "200", "500"
                }
                if ("border".equalsIgnoreCase(prop.getCode())) {
                    border = "true".equalsIgnoreCase(prop.getLabel()) || "yes".equalsIgnoreCase(prop.getLabel());
                }
            }
        }

        List<ProductionStep> workflowSteps = workflowEngine.getWorkflowForOrder(quality, border);
        List<OrderItem.Step> itemSteps = workflowSteps.stream().map(ws -> {
            OrderItem.Step s = new OrderItem.Step();
            s.setName(ws.getType()); // e.g., "PRINTING", "CUTTING"
            s.setState(OrderItem.StepState.PENDING);
            s.setOrder(ws.getPriority());
            return s;
        }).toList();

        item.setSteps(itemSteps);

        OrderItem saved = orderItemRepository.save(item);
        auditLogService.log(saved.getId(), "ORDER_ITEM", "CREATE", "Order Item created", "system");
        return saved;
    }

    @Transactional
    public OrderItem updateState(String id, OrderItem.OrderItemState newState) {
        OrderItem item = orderItemRepository.findById(id)
                .filter(i -> !Boolean.TRUE.equals(i.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + id));

        item.setState(newState);
        item.setUpdatedAt(LocalDateTime.now());

        auditLogService.log(id, "ORDER_ITEM", "UPDATE_STATE", "State updated to " + newState, "system");
        return orderItemRepository.save(item);
    }

    @Transactional
    public OrderItem requestReprint(String id, String reason) {
        OrderItem item = orderItemRepository.findById(id)
                .filter(i -> !Boolean.TRUE.equals(i.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + id));

        item.setReprint(true);
        OrderItem.ReprintState rs = new OrderItem.ReprintState();
        rs.setRequested(true);
        rs.setRequestedAt(LocalDateTime.now());
        rs.setReason(reason);
        rs.setPushed(false);
        item.setReprintState(rs);

        // Logic might require resetting state or creating new jobs?
        // For now, simpler implementation as requested.
        item.setUpdatedAt(LocalDateTime.now());

        auditLogService.log(id, "ORDER_ITEM", "REPRINT_REQUEST", "Reprint requested: " + reason, "system");
        return orderItemRepository.save(item);
    }

    @Transactional
    public OrderItem linkForms(String id, List<String> formIds) {
        OrderItem item = orderItemRepository.findById(id)
                .filter(i -> !Boolean.TRUE.equals(i.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + id));

        item.setFormIds(formIds);

        // Assume if forms are linked, we are in progress
        if (item.getState() == OrderItem.OrderItemState.PENDING) {
            item.setState(OrderItem.OrderItemState.IN_PROGRESS);
        }

        item.setUpdatedAt(LocalDateTime.now());
        auditLogService.log(id, "ORDER_ITEM", "LINK_FORMS", "Linked " + formIds.size() + " forms", "system");
        return orderItemRepository.save(item);
    }

    public Optional<OrderItem> getOrderItemById(String id) {
        return orderItemRepository.findById(id).filter(i -> !Boolean.TRUE.equals(i.getDeleted()));
    }

    public Optional<OrderItem> getOrderItemByItemId(String itemId) {
        return orderItemRepository.findByItemId(itemId).filter(i -> !Boolean.TRUE.equals(i.getDeleted()));
    }
}