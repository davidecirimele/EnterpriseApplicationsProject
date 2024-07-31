package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.TransactionsDao;
import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Transaction;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsDao transactionsDao;

    private final ModelMapper modelMapper;


    @Override
    public List<TransactionDto> getAllTransactionByUserId(UUID userId) {
        List<Transaction> transactions = transactionsDao.findAllByUserId(userId, Sort.by("date").descending());
        return transactions.stream().map(t -> modelMapper.map(t, TransactionDto.class)).toList();
    }

    @Override
    public Transaction addTransaction(User user, Order order, PaymentMethod paymentMethod, Double amount, PaymentStatus paymentStatus, LocalDate date) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setOrder(order);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setAmount(amount);
        transaction.setPaymentStatus(paymentStatus);
        transaction.setDate(date);
        transactionsDao.save(transaction);
        return transaction;
    }

}





















