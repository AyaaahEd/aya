package com.pfe.production.domain.orderitem;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
    Optional<OrderItem> findByItemId(String itemId);

    java.util.List<OrderItem> findByState(OrderItem.OrderItemState state);
}