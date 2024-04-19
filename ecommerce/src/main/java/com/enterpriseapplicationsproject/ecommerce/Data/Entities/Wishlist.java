package com.enterpriseapplicationsproject.ecommerce.Data.Entities;


import com.enterpriseapplicationsproject.ecommerce.Data.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name= "Wishlist", uniqueConstraints =
@UniqueConstraint(
        columnNames = {"USER_ID", "GROUP_ID"}
))
@Entity
public class Wishlist {
    @Id
    private long wishlistId;

    @OneToMany(mappedBy = "WISHLIST_ID")//mappedby indica il nome del campo nella tabella WishlistItems
    private List<WishlistItems> items = new ArrayList<>();


    @ManyToOne()
    @JoinColumn(
            name = "USER_ID",
            referencedColumnName = "ID"
    )
    private Users userId;//pk

    @OneToOne(mappedBy = "GROUP_ID", optional = false)//optional indica
    @JoinColumn(
            name = "GROUP_ID",
            referencedColumnName = "ID"
    )
    private Groups group;//pk
    //non posso mettere long perchè

    @Basic(optional = false)
    private String privacySetting;
}
