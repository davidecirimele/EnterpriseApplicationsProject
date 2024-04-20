package com.enterpriseapplicationsproject.ecommerce.Data.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class Product {

    @Id
    @GeneratedValue()
    @Column(name = "ID")
    private Long id;

}
