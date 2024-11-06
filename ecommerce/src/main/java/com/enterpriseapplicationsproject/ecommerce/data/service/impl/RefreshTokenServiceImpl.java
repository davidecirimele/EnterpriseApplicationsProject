package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.data.dao.RefreshTokensDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.RefreshToken;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import com.enterpriseapplicationsproject.ecommerce.exception.TokenNotFoundException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokensDao refreshTokenDao;

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public RefreshTokenDto save(RefreshTokenDto refreshTokenDto, UserDetails userDetails){

        try {
            User user = userDao.findByCredentialEmail(userDetails.getUsername()).orElseThrow(()->new UserNotFoundException("User not found"));

            RefreshToken refreshToken = convertDto(refreshTokenDto, user);
            RefreshToken r = refreshTokenDao.save(refreshToken);
            return modelMapper.map(r, RefreshTokenDto.class);
        }catch(Exception e){
            log.error("Unexpected error while saving refresh token for user with email: "+userDetails.getUsername()+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }

    }

    @Override
    public RefreshTokenDto getTokenByUserId(UUID userId) {

        try {
            User user = userDao.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));

            RefreshToken refreshToken = refreshTokenDao.findByUserId(userId)
                        .orElseThrow(() -> new TokenNotFoundException("Token not found"));

            return modelMapper.map(refreshToken, RefreshTokenDto.class);
        }catch(Exception e){
            log.error("Unexpected error while fetching refresh token by user with ID: "+userId+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    @Transactional
    public void revokeRefreshTokenByUserId(UUID userId) {
        try {
            refreshTokenDao.deleteByUserId(userId);
        }catch(Exception e){
            log.error("Unexpected error while revoking refresh token for user with ID: "+userId+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        try {
            RefreshToken refreshToken = refreshTokenDao.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token not found"));

            return modelMapper.map(refreshToken, RefreshTokenDto.class);
        }catch(Exception e){
            log.error("Unexpected error while fetching refresh token "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    @Transactional
    public void revokeRefreshTokenByToken(String token) {
        try {
            refreshTokenDao.deleteByToken(token);
        }catch(Exception e){
            log.error("Unexpected error while revoking refresh token "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public List<RefreshTokenDto> getAll() {
        try {
            List<RefreshToken> tokens = refreshTokenDao.findAll();
            return tokens.stream().map(token1 -> modelMapper.map(token1, RefreshTokenDto.class)).toList();
        }catch(Exception e){
            log.error("Unexpected error while fetching refresh tokens "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public RefreshToken convertDto(RefreshTokenDto refreshTokenDto, User user) {
        try {
            RefreshToken refreshToken = new RefreshToken();

            // Imposta il token
            refreshToken.setToken(refreshTokenDto.getToken());

            // Imposta l'utente associato
            refreshToken.setUser(user);

            return refreshToken;
        }catch(Exception e){
            log.error("Unexpected error while converting refresh token in Dto "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

}
