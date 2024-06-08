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
public interface UsersDao extends JpaRepository<User, Long> {

    //Visualizzare lista indirizzi associati
    List<Address> findAddressesById(Long userId);

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByCredentialEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :id")
    Optional<User> findByIdWithAddresses(@Param("id") Long id);

}
