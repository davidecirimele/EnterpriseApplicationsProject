package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class UserIdDto {

    @NotNull(message = "You must specify a user id")
    private UUID userId;

}
