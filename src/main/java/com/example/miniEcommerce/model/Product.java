package com.example.miniEcommerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;
}
