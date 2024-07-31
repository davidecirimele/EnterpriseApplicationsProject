package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionsDao extends CrudRepository<Transaction, Long> {


    List<Transaction> findAllByUserId(UUID userId, Sort date);
}
