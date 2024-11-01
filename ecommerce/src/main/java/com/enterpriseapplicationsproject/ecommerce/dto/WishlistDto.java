package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class WishlistDto {

    private Long id;

    private String name;

    private List<WishlistItemDto> items = new ArrayList<>();

    private UserDto user;

    private GroupDto group;

    private WishlistPrivacy privacySetting;

    private String wishlistToken;
}
