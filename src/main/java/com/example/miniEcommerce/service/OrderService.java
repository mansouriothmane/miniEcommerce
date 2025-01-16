package com.example.miniEcommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.miniEcommerce.model.Order;
import com.example.miniEcommerce.model.OrderItem;
import com.example.miniEcommerce.model.Product;
import com.example.miniEcommerce.model.User;
import com.example.miniEcommerce.repository.OrderItemRepository;
import com.example.miniEcommerce.repository.OrderRepository;
import com.example.miniEcommerce.repository.ProductRepository;

//TODO: make methods transactional
//TODO: add order status (PENDING, SHIPPED, DELIVERED, CANCELLED)

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public Order createOrder(User user, List<OrderItem> items) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        // Save the order first so it has an ID
        Order savedOrder = orderRepository.save(order);

        // Attach items to the order
        for (OrderItem item : items) {
            item.setOrder(savedOrder);

            // Decrement product stock
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
