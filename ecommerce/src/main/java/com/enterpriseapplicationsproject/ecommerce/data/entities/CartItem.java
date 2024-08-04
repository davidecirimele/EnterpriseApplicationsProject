package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CartItems")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CART_ID", referencedColumnName = "ID")
    private ShoppingCart cartId;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
    private Book bookId;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "ADD_DATE")
    private LocalDateTime addDate;

    @Column(name = "PRICE")
    private Double price;

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", bookId=" + (bookId != null ? bookId.getId() : null) +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
