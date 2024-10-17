package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ShoppingCarts")
@Data
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User userId;

    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> cartItems;

    @Transient
    private Double total;


    public Double getTotal() {

        if (cartItems == null || cartItems.isEmpty()) {
            return 0.0;
        }

        return this.total =  cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }



    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", userId=" + (userId != null ? userId.getId() : null) +
                ", total=" + total +
                '}';
    }
}
