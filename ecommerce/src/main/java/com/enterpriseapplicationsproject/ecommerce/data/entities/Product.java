package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "WEIGHT")
    private Double weight;

    @Basic(optional = false)
    @Column(name = "INSERT_DATE")
    private Date instertDate;

    @Basic(optional = false)
    @Column(name = "PRICE")
    private Double price;

    @Basic(optional = false)
    @Column(name = "STOCK")
    private Integer stock;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<OrderItems> orderItems = new ArrayList<>();

}
