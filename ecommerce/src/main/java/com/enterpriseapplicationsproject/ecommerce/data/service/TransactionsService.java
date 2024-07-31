package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Transaction;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionsService {

    public TransactionDto addTransactionDto(TransactionDto transactionDto);

    public List<TransactionDto> getAllTransactionByUserId(UUID userId);

    public Transaction addTransaction(User user, Order order, PaymentMethod paymentMethod, Double amount, PaymentStatus paymentStatus, LocalDate date);

}
