package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;

import java.util.List;

public interface TransactionsService {

    public TransactionDto addTransactionDto(TransactionDto transactionDto);

    public List<TransactionDto> getAllTransactionByUserId(Long userId);

}
