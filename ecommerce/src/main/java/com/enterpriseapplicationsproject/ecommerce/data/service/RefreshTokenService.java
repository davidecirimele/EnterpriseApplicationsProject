package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.data.entities.RefreshToken;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.CheckoutRequestDto;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveOrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    RefreshTokenDto save(RefreshTokenDto refreshTokenDto, UserDetails userDetails);

    RefreshTokenDto getTokenByUserId(UUID userId);

    void revokeRefreshTokenByUserId(UUID userId);

    RefreshTokenDto findByToken(String refreshToken);

    void revokeRefreshTokenByToken(String token);

    List<RefreshTokenDto> getAll();

    RefreshToken convertDto(RefreshTokenDto refreshTokenDto, User user);
}
