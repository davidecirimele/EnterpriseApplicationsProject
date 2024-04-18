package com.enterpriseapplicationsproject.ecommerce.Data.Entities;


import com.enterpriseapplicationsproject.ecommerce.Data.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name= "wishlists", uniqueConstraints =
@UniqueConstraint(
        columnNames = {"userId", "groupId"}
))
@Entity
public class Wishlist {
    @Id
    private long wishlistId;

    @OneToMany(mappedBy = "wishlistId")//mappedby indica il nome del campo nella tabella WishlistItems
    private List<WishlistItems> items = new ArrayList<>();


    @ManyToOne()
    @JoinColumn(referencedColumnName = "userId")
    private Users userId;//pk

    @OneToOne(mappedBy = "groupId", optional = false)//optional indica
    @JoinColumn(
            name = "groupId",
            referencedColumnName = "groupId"
    )
    private Groups group;//pk
    //non posso mettere long perchè

    @Basic(optional = false)
    private String privacySetting;
}
