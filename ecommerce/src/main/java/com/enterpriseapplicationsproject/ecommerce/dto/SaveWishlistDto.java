package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class SaveWishlistDto {

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Size(min = 0)
    private List<UserDto> items;

    @NotNull(message = "User is mandatory")
    private UserDto user;

    private GroupDto group;

    @NotNull(message = "Group is mandatory")
    private WishlistPrivacy privacySetting;

    @Size(min = 0)
    @NotNull(message = "Wishlist token is mandatory")
    private String wishlistToken;

}
