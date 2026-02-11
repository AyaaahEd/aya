package com.pfe.production.domain.orderitem;

import com.pfe.production.domain.orderitem.OrderItem.ConfigurationProperty;
import com.pfe.production.domain.orderitem.OrderItem.FileMeta;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderItemRequest(
                @NotEmpty(message = "Item ID is required") String itemId,

                @NotEmpty(message = "Order Number is required") String orderNumber,

                FileMeta thumbnail,

                LocalDateTime promiseAvailableDate,

                List<ConfigurationProperty> configurationProperties) {
}
