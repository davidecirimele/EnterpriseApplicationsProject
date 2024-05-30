package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class WishlistDto {

    private Long id;

    private List<WishlistItem> items = new ArrayList<>();

    private User userId;

    private Group group;

    private String privacySetting;
}
