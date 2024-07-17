package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionsService;
import com.enterpriseapplicationsproject.ecommerce.dto.TransactionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/transactions", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionsService transactionsService;

    @PostMapping(consumes =  "application/json", path = "/add")
    public ResponseEntity<TransactionDto> addTransaction( @Valid @RequestBody TransactionDto transactionDto){
        TransactionDto addeddTransaction = transactionsService.addTransactionDto(transactionDto);
        return new ResponseEntity<>(addeddTransaction, HttpStatus.CREATED);

    }

    @GetMapping(consumes =  "application/json", path = "/get/{userId}")
    public ResponseEntity<List<TransactionDto>> getUserTransactions(@PathVariable Long userId){
        List<TransactionDto> transactions = transactionsService.getAllTransactionByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
