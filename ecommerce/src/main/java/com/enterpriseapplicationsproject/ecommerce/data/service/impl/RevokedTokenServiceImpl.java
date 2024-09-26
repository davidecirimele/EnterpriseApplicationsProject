package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.RevokedTokensDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.RevokedToken;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.RevokedTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevokedTokenServiceImpl implements RevokedTokenService {
    @Autowired
    private RevokedTokensDao revokedTokensDao;

    @Transactional
    public void revokeToken(String token) {
        RevokedToken revokedToken = new RevokedToken(token);
        revokedTokensDao.save(revokedToken);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokensDao.existsByToken(token);
    }
}
