package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetails;
import com.enterpriseapplicationsproject.ecommerce.data.dao.RefreshTokensDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.RefreshToken;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.RefreshTokenService;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.security.RefreshTokenDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokensDao refreshTokenDao;

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public RefreshTokenDto save(RefreshTokenDto refreshTokenDto, UserDetails userDetails){
        Optional<User> optionalUser = userDao.findByCredentialEmail(userDetails.getUsername());

        if(optionalUser.isPresent()){
            RefreshToken refreshToken = convertDto(refreshTokenDto, optionalUser.get());
            RefreshToken r = refreshTokenDao.save(refreshToken);
            return modelMapper.map(r, RefreshTokenDto.class);
        }
        else{
            throw new RuntimeException("User not found");
        }

    }

    @Override
    public RefreshTokenDto getTokenByUserId(UUID userId) {
        Optional<User> optionalUser = userDao.findById(userId);

        if(optionalUser.isPresent()){
            RefreshToken refreshToken = refreshTokenDao.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Token not found"));
            return modelMapper.map(refreshToken, RefreshTokenDto.class);
        }
        else{
            throw new RuntimeException("User not found");
        }
    }

    @Override
    @Transactional
    public void revokeRefreshTokenByUserId(UUID userId) {
        refreshTokenDao.deleteByUserId(userId);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenDao.findByToken(token);

        return optionalRefreshToken.map(refreshToken -> modelMapper.map(refreshToken, RefreshTokenDto.class)).orElse(null);
    }

    @Override
    @Transactional
    public void revokeRefreshTokenByToken(String token) {
        refreshTokenDao.deleteByToken(token);
    }

    @Override
    public List<RefreshTokenDto> getAll() {
        List<RefreshToken> tokens = refreshTokenDao.findAll();
        return tokens.stream().map(token1 -> modelMapper.map(token1, RefreshTokenDto.class)).toList();
    }

    @Override
    public RefreshToken convertDto(RefreshTokenDto refreshTokenDto, User user) {
            RefreshToken refreshToken = new RefreshToken();

            // Imposta il token
            refreshToken.setToken(refreshTokenDto.getToken());

            // Imposta l'utente associato
            refreshToken.setUser(user);

            // Imposta lo stato di validità (puoi decidere come impostarlo)
            refreshToken.set_valid(true);

            return refreshToken;
    }

}
