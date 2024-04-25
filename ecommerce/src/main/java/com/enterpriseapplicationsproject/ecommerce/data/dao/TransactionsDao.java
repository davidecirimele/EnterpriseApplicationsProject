package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionsDao extends CrudRepository<Transaction, Long> {
}
