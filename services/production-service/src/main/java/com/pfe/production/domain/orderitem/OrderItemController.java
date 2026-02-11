package com.pfe.production.domain.orderitem;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/production/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItemResponse> createOrderItem(@Valid @RequestBody CreateOrderItemRequest request) {
        try {
            OrderItem item = orderItemService.createOrderItem(request);
            return ResponseEntity.ok(OrderItemResponse.fromEntity(item));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/state")
    public ResponseEntity<OrderItemResponse> updateState(@PathVariable String id,
            @RequestBody Map<String, String> payload) {
        try {
            String stateStr = payload.get("state");
            OrderItem.OrderItemState newState = OrderItem.OrderItemState.valueOf(stateStr);
            return ResponseEntity.ok(OrderItemResponse.fromEntity(orderItemService.updateState(id, newState)));
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/reprint")
    public ResponseEntity<OrderItemResponse> requestReprint(@PathVariable String id,
            @RequestBody Map<String, String> payload) {
        try {
            String reason = payload.get("reason");
            return ResponseEntity.ok(OrderItemResponse.fromEntity(orderItemService.requestReprint(id, reason)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable String id) {
        return orderItemService.getOrderItemById(id)
                .map(OrderItemResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<OrderItemResponse> getOrderItemByItemId(@PathVariable String itemId) {
        return orderItemService.getOrderItemByItemId(itemId)
                .map(OrderItemResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}