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

    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    @Transient
    private Double total;


    @PreUpdate
    @PrePersist
    private void calculateTotal() {
        Double totalf = 0.0;

        if(cartItems == null)
            cartItems = new ArrayList<CartItem>();

        for (CartItem item : cartItems) {
            total += item.getBookId().getPrice() * item.getQuantity();
        }
        this.total = totalf;
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
