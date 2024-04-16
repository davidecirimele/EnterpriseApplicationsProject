package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ShoppingCarts")
public class ShoppingCarts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private Users user_id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
