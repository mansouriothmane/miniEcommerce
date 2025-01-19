package com.example.miniEcommerce.controller;

import com.example.miniEcommerce.model.Order;
import com.example.miniEcommerce.model.OrderItem;
import com.example.miniEcommerce.model.User;
import com.example.miniEcommerce.service.OrderService;
import com.example.miniEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // CREATE
    @PostMapping("/create/{userId}")
    public ResponseEntity<Order> createOrder(
            @PathVariable Long userId,
            @RequestBody List<OrderItem> items) {
        User user = userService.getUserById(userId);
        Order newOrder = orderService.createOrder(user, items);
        return ResponseEntity.ok(newOrder);
    }

    // READ: Get a specific order
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // READ: All orders for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }
}
