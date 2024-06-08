package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressesDao extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.userId.id = :userId")
    List<Address> findByUserId(@Param("userId") Long userId);

}
