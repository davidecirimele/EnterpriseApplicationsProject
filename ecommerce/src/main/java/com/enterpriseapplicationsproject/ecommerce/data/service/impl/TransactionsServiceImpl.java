package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.TransactionsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Transaction;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsDao transactionsDao;

    private final ModelMapper modelMapper;

    private final UserService userService;


    @Override
    public TransactionDto addTransactionDto(TransactionDto transactionDto) {

        //User user = userService.getUserByEmail(email).orElseThrow( () -> new RuntimeException("User not found"));
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);

        //transaction.setUser(user.getId);

        Transaction t = transactionsDao.save(transaction);
        return modelMapper.map(t, TransactionDto.class);
    }

    @Override
    public List<TransactionDto> getAllTransactionByUserId(Long userId) {
        List<Transaction> transactions = transactionsDao.findAllByUserId(userId, Sort.by("date").descending());
        return transactions.stream().map(t -> modelMapper.map(t, TransactionDto.class)).toList();
    }

    /*@Override
    public List<TransactionDto> getAllTransactionByUserEmail(String email) {
        User user = userService.getUserByEmail(email).orElseThrow( () -> new RuntimeException("User not found"));
        List<Transaction> transactions = transactionsDao.findAllByUserId(user.getId(), Sort.by("date").descending());
        return transactions.stream().map(t -> modelMapper.map(t, TransactionDto.class)).toList();
    }*/

}





















