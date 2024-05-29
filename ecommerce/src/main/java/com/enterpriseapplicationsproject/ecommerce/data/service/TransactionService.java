package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Transaction;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    public TransactionDto addTransactionDto(TransactionDto transactionDto);

    public List<TransactionDto> getAllTransactionByUserId(Long userId);

    public List<TransactionDto> getAllTransactionByUserEmail(String email);
}
