package com.enterpriseapplicationsproject.ecommerce.data.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name= "Wishlists", uniqueConstraints =
@UniqueConstraint(
        columnNames = {"USER_ID", "GROUP_ID"}
))
@Entity
@Data
@NoArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToMany(mappedBy = "wishlist")//mappedBy indica il nome dell'attributo nella classe WishlistItem
    private List<WishlistItem> items = new ArrayList<>();


    @ManyToOne()
    @JoinColumn(
            name = "USER_ID",
            referencedColumnName = "ID"
    )
    private User userId;//pk

    @OneToOne(optional = false)//optional indica
    @JoinColumn(
            name = "GROUP_ID",
            referencedColumnName = "ID"
    )
    private Group group;//pk
    //non posso mettere long perchè

    @Basic(optional = false)
    private String privacySetting;
}