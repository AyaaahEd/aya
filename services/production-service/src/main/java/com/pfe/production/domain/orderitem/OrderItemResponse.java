package com.pfe.production.domain.orderitem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderItemResponse(
        String id,
        String itemId,
        String orderNumber,
        OrderItem.FileMeta thumbnail,
        List<OrderItem.Step> steps,
        List<String> formIds,
        OrderItem.OrderItemState state,
        Boolean reprint,
        OrderItem.ReprintState reprintState,
        LocalDateTime promiseAvailableDate,
        List<OrderItem.ConfigurationProperty> configurationProperties,
        Boolean closed,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static OrderItemResponse fromEntity(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getItemId(),
                item.getOrderNumber(),
                item.getThumbnail(),
                item.getSteps(),
                item.getFormIds(),
                item.getState(),
                item.getReprint(),
                item.getReprintState(),
                item.getPromiseAvailableDate(),
                item.getConfigurationProperties(),
                item.getClosed(),
                item.getCreatedBy(),
                item.getCreatedAt(),
                item.getUpdatedAt());
    }
}