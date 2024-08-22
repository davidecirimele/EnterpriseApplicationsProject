package com.enterpriseapplicationsproject.ecommerce.data.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WishlistItems")
@Data
@NoArgsConstructor
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "WISHLIST_ID",
            referencedColumnName = "ID" // indica la colonna a cui fa riferimento
    )
    @JsonBackReference
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(
            name = "BOOK_ID",
            referencedColumnName = "ID"
    )
    private Book book;

    @Column(name = "QUANTITY")
    private int quantity;
}