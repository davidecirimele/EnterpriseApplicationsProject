package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressesDao extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.userId.id = :userId")
    List<Address> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Address a WHERE a.id = :id AND a.valid = true")
    Optional<Address> isValidByAddressId(@Param("id") Long id);

    @Query("SELECT a FROM Address a WHERE a.userId.id = :userId AND a.defaultAddress = true")
    List<Address> findAllByDefaultAddress(@Param("userId") Long userId);

    @Query("SELECT a FROM Address a WHERE a.userId.id = :userId AND a.valid = true")
    List<Address> findAllByValidity(@Param("userId") Long userid);

    @Query("SELECT u FROM User u JOIN u.addresses a WHERE a.id = :id")
    User findUserByAddressId(@Param("id") Long id);
}
