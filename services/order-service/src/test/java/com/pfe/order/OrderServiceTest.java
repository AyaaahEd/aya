package com.pfe.order;

import com.pfe.order.domain.Order;
import com.pfe.order.domain.OrderItem;
import com.pfe.order.repository.OrderRepository;
import com.pfe.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void createOrder_shouldSaveOrderWithStatusAndDate() {
        OrderItem item = new OrderItem();
        item.setProductId("prod1");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("10.00"));

        Order order = new Order();
        order.setUserId("user1");
        order.setItems(List.of(item));
        order.setTotalAmount(new BigDecimal("20.00"));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder.getCreatedAt());
        assertEquals("PENDING", savedOrder.getStatus());
        assertEquals("user1", savedOrder.getUserId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void getOrdersByUserId_shouldReturnUserOrders() {
        Order order1 = new Order();
        order1.setUserId("user1");
        Order order2 = new Order();
        order2.setUserId("user1");

        when(orderRepository.findByUserId("user1")).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getOrdersByUserId("user1");

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findByUserId("user1");
    }
}
