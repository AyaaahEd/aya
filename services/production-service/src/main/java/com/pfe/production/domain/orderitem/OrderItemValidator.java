package com.pfe.production.domain.orderitem;

import com.pfe.production.domain.orderitem.OrderItem.OrderItemState;
import com.pfe.production.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * Domain validator for OrderItem aggregate.
 * Encapsulates business rules from Section 12.5 of the DDD spec.
 */
@Component
public class OrderItemValidator {

    /**
     * Validates an order item can be closed (must be COMPLETED).
     */
    public void validateCanClose(OrderItem item) {
        if (item.getState() != OrderItemState.COMPLETED) {
            throw new BusinessException(
                    "Order item must be COMPLETED to close. Current state: " + item.getState());
        }
        if (Boolean.TRUE.equals(item.getClosed())) {
            throw new BusinessException("Order item is already closed");
        }
    }

    /**
     * Closed order items are read-only.
     */
    public void validateNotClosed(OrderItem item) {
        if (Boolean.TRUE.equals(item.getClosed())) {
            throw new BusinessException("Cannot modify a closed order item: " + item.getItemId());
        }
    }

    /**
     * Validates order item is not deleted.
     */
    public void validateNotDeleted(OrderItem item) {
        if (Boolean.TRUE.equals(item.getDeleted())) {
            throw new BusinessException("Cannot modify a deleted order item: " + item.getItemId());
        }
    }

    /**
     * Validates a state transition is allowed.
     * PENDING → IN_PROGRESS → COMPLETED → SHIPPED
     */
    public void validateStateTransition(OrderItemState from, OrderItemState to) {
        boolean valid = switch (from) {
            case PENDING -> to == OrderItemState.IN_PROGRESS;
            case IN_PROGRESS -> to == OrderItemState.COMPLETED;
            case COMPLETED -> to == OrderItemState.SHIPPED;
            case SHIPPED -> false;
        };

        if (!valid) {
            throw new BusinessException(
                    "Invalid order item state transition: " + from + " → " + to);
        }
    }
}