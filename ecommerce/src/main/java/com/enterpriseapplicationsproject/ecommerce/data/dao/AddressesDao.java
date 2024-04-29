package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressesDao extends JpaRepository<Address, Long> {
}
