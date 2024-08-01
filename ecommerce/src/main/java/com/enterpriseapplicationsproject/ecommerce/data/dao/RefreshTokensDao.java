package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Product;
import com.enterpriseapplicationsproject.ecommerce.data.entities.RefreshToken;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokensDao extends JpaRepository<RefreshToken, UUID> {

    @Query("SELECT r FROM RefreshToken r WHERE r.user.id = :id")
    Optional<RefreshToken> findByUserId(UUID id);

    @Query("DELETE FROM RefreshToken r WHERE r.user.id = :id")
    void deleteByUserId(@Param("id") UUID id);

    Optional<RefreshToken>findByToken(String token);

    void deleteByToken(String token);
}
