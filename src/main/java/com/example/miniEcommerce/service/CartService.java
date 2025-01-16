package com.example.miniEcommerce.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.miniEcommerce.model.Cart;
import com.example.miniEcommerce.model.CartItem;
import com.example.miniEcommerce.model.Product;
import com.example.miniEcommerce.model.User;
import com.example.miniEcommerce.repository.CartItemRepository;
import com.example.miniEcommerce.repository.CartRepository;
import com.example.miniEcommerce.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get or create a cart for the user
     */
    public Cart getCartForUser(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });
    }

    /**
     * Add a product to the user's cart, or update quantity if it already exists
     */
    public Cart addItemToCart(User user, Long productId, int quantity) {
        Cart cart = getCartForUser(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item is already in cart
        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // Update quantity
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            cartItemRepository.save(existingItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf((quantity))));
            cartItemRepository.save(cartItem);
        }

        // Recalculate total price
        recalculateCartTotal(cart);
        return cartRepository.save(cart);
    }

    /**
     * Remove or decrement an item from the cart
     */
    public Cart removeItemFromCart(User user, Long productId, int quantityToRemove) {
        Cart cart = getCartForUser(user);

        // Check if item is in cart
        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            int newQuantity = item.getQuantity() - quantityToRemove;
            if (newQuantity <= 0) {
                // Remove the item entirely
                cartItemRepository.delete(item);
            } else {
                // Update quantity
                item.setQuantity(newQuantity);
                item.setPrice(item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(newQuantity)));
                cartItemRepository.save(item);
            }
            recalculateCartTotal(cart);
            cartRepository.save(cart);
        }

        return cart;
    }

    public void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public void recalculateCartTotal(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }
}
