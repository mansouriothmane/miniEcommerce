package com.example.miniEcommerce.controller;

import com.example.miniEcommerce.model.Cart;
import com.example.miniEcommerce.model.User;
import com.example.miniEcommerce.service.CartService;
import com.example.miniEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    // GET user's cart
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        Cart cart = cartService.getCartForUser(user);
        return ResponseEntity.ok(cart);
    }

    // ADD item to cart
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        User user = userService.getUserById(userId);
        Cart updatedCart = cartService.addItemToCart(user, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // REMOVE item from cart
    @PostMapping("/{userId}/remove")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        User user = userService.getUserById(userId);
        Cart updatedCart = cartService.removeItemFromCart(user, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // CLEAR cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        Cart cart = cartService.getCartForUser(user);
        cartService.clearCart(cart);
        return ResponseEntity.noContent().build();
    }
}
