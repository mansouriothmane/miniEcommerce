package com.example.miniEcommerce.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // List of cart items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    // Could store total price or compute on the fly
    private Double totalPrice;

    public Cart() {
    }
}
