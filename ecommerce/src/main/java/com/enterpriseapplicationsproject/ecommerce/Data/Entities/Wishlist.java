package com.enterpriseapplicationsproject.ecommerce.Data.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"userId", "groupId"}
))
@Entity
public class Wishlist {
    @Id
    private long wishlistId;

    @ManyToOne()
    @JoinColumn(referencedColumnName = "userId")
    private User userId;//pk

    @OneToOne(mappedBy = "wishlistId")
    private long groupId;//pk
    //non posso mettere long perchè

    @Basic(optional = false)
    private String privacySetting;
}
