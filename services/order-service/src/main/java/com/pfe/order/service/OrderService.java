package com.pfe.order.service;

import com.pfe.order.domain.Order;
import com.pfe.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderService(OrderRepository orderRepository,
            org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClient = webClientBuilder.build();
    }

    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");
        Order savedOrder = orderRepository.save(order);

        // 1. Create Production Job (Sync)
        try {
            // Simply sending basic info, assuming Production Service handles steps
            // Using Map for flexibility or we could define a DTO
            java.util.Map<String, Object> jobPayload = java.util.Map.of(
                    "orderId", savedOrder.getId(),
                    "status", "PENDING");

            webClient.post()
                    .uri("http://production-service:8085/api/production/jobs")
                    .bodyValue(jobPayload)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe(); // Fire and forget for now, or block() if strictly sync

        } catch (Exception e) {
            System.err.println("Failed to create Production Job: " + e.getMessage());
        }

        // 2. Create Invoice (Sync)
        try {
            java.util.Map<String, Object> invoicePayload = java.util.Map.of(
                    "orderId", savedOrder.getId(),
                    "userId", savedOrder.getUserId(),
                    "amount", savedOrder.getTotalAmount(),
                    "status", "PENDING");

            webClient.post()
                    .uri("http://invoice-service:8084/api/invoices")
                    .bodyValue(invoicePayload)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();

        } catch (Exception e) {
            System.err.println("Failed to create Invoice: " + e.getMessage());
        }

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
