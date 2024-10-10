package com.enterpriseapplicationsproject.ecommerce.data.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Base64;
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

    @OneToMany(mappedBy = "wishlist", fetch = FetchType.EAGER)//mappedBy indica il nome dell'attributo nella classe WishlistItem
    @JsonManagedReference
    private List<WishlistItem> items = new ArrayList<>();

    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    @ManyToOne()
    @JoinColumn(
            name = "USER_ID",
            referencedColumnName = "ID"
    )
    private User userId;//

    @OneToOne(optional = false)//optional indica
    @JoinColumn(
            name = "GROUP_ID",
            referencedColumnName = "ID"
    )
    private Group group;//pk
    //non posso mettere long perchè

    @Basic(optional = false)
    private String privacySetting;

     //@Basic(optional = false)
     @Access(AccessType.FIELD)
    @Column(name = "W_TOKEN",unique = true)
    private String wToken;
}