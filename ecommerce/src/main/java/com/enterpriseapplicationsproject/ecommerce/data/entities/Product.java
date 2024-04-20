package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class Product {

    @Id
    private long productId;

}
